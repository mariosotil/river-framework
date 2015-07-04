package org.riverframework.extended;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;
import org.riverframework.core.Database;
import org.riverframework.core.Document;
import org.riverframework.core.DocumentIterator;
import org.riverframework.core.Field;
import org.riverframework.core.Session;
import org.riverframework.core.View;
import org.riverframework.extended.AbstractDatabase;
import org.riverframework.extended.AbstractDocument;

public abstract class AbstractDatabaseTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	protected Session session = null;
	protected VacationDatabase vacationDatabase = null;
	protected Context context = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				Class<?> clazz = Class.forName(this.getClass().getPackage().getName() + ".Context");
				if (org.riverframework.Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = context.getSession();
				vacationDatabase = session.getDatabase(VacationDatabase.class, context.getTestDatabaseServer(),
						context.getTestDatabasePath());
				vacationDatabase.deleteAll();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void close() {
		context.closeSession();
	}

	@Test
	public void testOpenedDatabase() {
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());
		assertFalse("The file path could not be detected.", vacationDatabase.getFilePath().equals(""));
		assertFalse("The database name could not be detected.", vacationDatabase.getName().equals(""));
	}

	@Test
	public void testCreateAndGetView() {
		assertTrue("The test database could not be instantiated.", vacationDatabase != null);
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View view = vacationDatabase.createView(name, "SELECT Form = \"" + form + "\"");

		assertTrue("There is a problem creating the view in the test database.", view.isOpen());

		view.close();
		view = null;

		int i = 0;
		for (i = 0; i < 10; i++) {
			vacationDatabase.createDocument().setField("Form", form).setField("Value", i).save();
		}

		view = vacationDatabase.getView(name);
		assertTrue("There is a problem opening the last view created in the test database.", view.isOpen());

		DocumentIterator it = view.getAllDocuments();

		i = 0;
		while (it.hasNext()) {
			i++;
			it.next();
		}
		assertTrue("There is a problem with the documents indexed in the last view.", i == 10);

		it = view.getAllDocuments();

		i = 0;
		while (it.hasNext()) {
			i++;
			Document doc = it.next();
			doc.delete();
		}

		view.refresh();
		i = 0;
		it = view.getAllDocuments();
		while (it.hasNext()) {
			i++;
			it.next();
		}

		assertTrue("There is a problem with the last documents created when we try to delete them.", i == 0);

		view.delete();

		assertFalse("There is a problem deleting the last view created.", view.isOpen());
	}

	static class VacationDatabase extends AbstractDatabase<VacationDatabase> {

		protected VacationDatabase(Database database) {
			super(database);
		}

		@Override
		protected VacationDatabase getThis() {
			return this;
		}
	}

	static class VacationRequest extends AbstractDocument<VacationRequest> {
		protected VacationRequest(Document doc) {
			super(doc);
		}

		protected VacationRequest init() {
			doc.setField("Form", "fo_vacation_request")
			.setField("TEST_FIELD", "YES")
			.setField("Form", "TestForm");

			return getThis();
		}

		@Override
		protected VacationRequest getThis() {
			return this;
		}
	}

	@Test
	public void testCreateAndGetVacationRequest() {
		assertTrue("The test database could not be opened as a VacationDatabase.", vacationDatabase.isOpen());

		VacationRequest doc = vacationDatabase.createDocument(VacationRequest.class);

		assertTrue("There is a problem creating a new VacationRequest in the test database.", doc.isOpen());

		String universalId = doc.init().save().getObjectId();

		doc = null;
		doc = vacationDatabase.getDocument(VacationRequest.class, universalId);

		assertTrue("There is a problem getting a VacationRequest created in the test database.", doc.isOpen());
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be instantiated.", vacationDatabase != null);
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());

		DocumentIterator col = vacationDatabase.getAllDocuments().deleteAll();

		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			vacationDatabase.createDocument().setField("Form", TEST_FORM).setField("Value", rs.nextString()).save();
		}

		vacationDatabase.createDocument().setField("Form", TEST_FORM).setField("Value", "THIS_IS_THE_DOC").save();

		vacationDatabase.refreshSearchIndex();

		col = null;
		col = vacationDatabase.search("THIS IS IMPOSSIBLE TO FIND");
		assertTrue("The search returns values for a query that would returns nothing.", !col.hasNext());

		col = null;
		col = vacationDatabase.search("THIS_IS_THE_DOC");
		assertTrue("The search does not returns values for a query that would returns something.", col.hasNext());
	}

	@Test
	public void testGetDocumentCollection() {
		assertTrue("The vacation database could not be instantiated.", vacationDatabase != null);
		assertTrue("The vacation database could not be opened.", vacationDatabase.isOpen());

		vacationDatabase.deleteAll();

		vacationDatabase.createDocument().setField("Requester", "John").setField("Time", 30).save();

		vacationDatabase.createDocument().setField("Requester", "Kathy").setField("Time", 25).save();

		vacationDatabase.createDocument().setField("Requester", "Michael").setField("Time", 27).save();

		DocumentIterator col = vacationDatabase.getAllDocuments();

		for (Document doc : col) {
			assertTrue("It could not possible load the vacation request object from the DocumentList.", doc.isOpen());
			assertTrue("The vacation request object from the DocumentList is an instance from " + doc.getClass().getName()
					+ ", and not from VacationRequest.", doc.getClass().getSimpleName().contains("VacationRequest"));
		}
	}

	static class Person extends AbstractDocument<Person> implements Unique<Person> {
		protected Person(Document doc) {
			super(doc);
		}

		@Override
		public String getIndexName() {
			return "vi_ap_people_index";
		}

		@Override
		public String getId() {
			return doc.getFieldAsString("ca_pe_name");
		}

		@Override
		public Person generateId() {
			// Do nothing
			return this;
		}

		@Override
		public Person setId(String arg0) {
			doc.setField("ca_pe_name", arg0);
			return this;
		}

		@Override
		protected Person getThis() {
			return this;
		}

	}

	@Test
	public void testGetDocument() {
		assertTrue("The test database could not be instantiated.", vacationDatabase != null);
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());

		DocumentIterator iterator = vacationDatabase.getAllDocuments().deleteAll();
		iterator = vacationDatabase.getAllDocuments();
		assertFalse("The database still has documents.", iterator.hasNext());

		vacationDatabase.createDocument(Person.class).setId("John").setField("Form", "fo_ap_people").setField("Age", 30).save();

		vacationDatabase.createDocument(Person.class).setId("Kathy").setField("Form", "fo_ap_people").setField("Age", 25).save();

		vacationDatabase.createDocument(Person.class).setId("Jake").setField("Form", "fo_ap_people").setField("Age", 27).save();

		Person p = vacationDatabase.getDocument(Person.class, "Jake");
		assertTrue("It could not possible load the person object for Jake.", p.isOpen());
		assertTrue("It could not possible get the Jake's age.", p.getFieldAsInteger("Age") == 27);

		p = vacationDatabase.getDocument(Person.class, "John");
		assertTrue("It could not possible load the person object for John.", p.isOpen());
		assertTrue("It could not possible get the John's age.", p.getFieldAsInteger("Age") == 30);


		p = vacationDatabase.getDocument(Person.class, "Kathy");
		assertTrue("It could not possible load the person object for Kathy.", p.isOpen());
		assertTrue("It could not possible get the Kathy's age.", p.getFieldAsInteger("Age") == 25);

		String unid = p.getObjectId();
		p = null;
		p = vacationDatabase.getDocument(Person.class, unid);
		assertTrue("It should be possible to load a person object for Kathy with its Universal Id.", p.isOpen());

		p = null;
		p = vacationDatabase.getDocument(Person.class, "Kathy");
		assertFalse("It should not be possible to load a person object for Kathy without indicate its class.", p.isOpen());
	}

	@Test
	public void testGetFields() {
		assertTrue("The test database could not be instantiated.", vacationDatabase != null);
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());

		vacationDatabase.getAllDocuments().deleteAll();

		vacationDatabase.createDocument(Person.class).setId("Kathy").setField("Form", "fo_ap_people").setField("Age", 25).save().close();

		Document p = vacationDatabase.getDocument(Person.class, "Kathy");
		assertTrue("It could not possible load the person object for Kathy.", p.isOpen());

		Map<String, Field> fields = p.getFields();

		assertTrue("It could not possible get the fields from the Kathy's document.", fields.size() > 0);

		Field field = fields.get("ca_pe_name");
		String value = field.get(0).toString();

		assertTrue("It could not possible get the field Name from the Kathy's document.", value.equals("Kathy"));

		field = fields.get("Age");
		int age = ((Double) field.get(0)).intValue();

		assertTrue("It could not possible get the field Age from the Kathy's document.", age == 25);

	}

}
