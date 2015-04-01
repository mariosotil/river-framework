package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentCollection;
import org.riverframework.RandomString;
import org.riverframework.Session;
import org.riverframework.Unique;
import org.riverframework.View;

public abstract class AbstractDatabaseTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	protected Session session = null;
	protected Database database = null;
	protected Database vacationDatabase = null;
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
				database = session.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());
				vacationDatabase = session.getDatabase(VacationDatabase.class, context.getTestDatabaseServer(),
						context.getTestDatabasePath());
				database.getAllDocuments().deleteAll();
				vacationDatabase.getAllDocuments().deleteAll();
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
		assertTrue("The test database could not be opened.", database.isOpen());
		assertFalse("The file path could not be detected.", database.getFilePath().equals(""));
		assertFalse("The database name could not be detected.", database.getName().equals(""));
	}

	// @Test
	// public void testCreateAndGetGraph() {
	// assertTrue("The test database could not be opened.", rDatabase.isOpen());
	//
	// org.riverframework.development.Relation rRelation = rDatabase.createRelation(TEST_GRAPH);
	//
	// assertTrue("There is a problem creating a new graph in the test database.", rRelation.isOpen());
	//
	// String universalId = rRelation.save(org.riverframework.Document.FORCE_SAVE).getUniversalId();
	// rRelation = null;
	//
	// rRelation = rDatabase.getRelation(universalId);
	//
	// assertTrue("There is a problem getting the graph created in the test database.",
	// rRelation.isOpen());
	// }

	@Test
	public void testGetView() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		View view = database.getView(DefaultView.class, TEST_VIEW);

		assertTrue("There is a problem getting the view created in the test database.", view.isOpen());
	}

	static class VacationDatabase extends DefaultDatabase {
		protected VacationDatabase(Session s, org.riverframework.module.Database obj) {
			super(s, obj);
		}

		@Override
		public Class<? extends org.riverframework.Document> detectClass(org.riverframework.module.Document _doc) {
			String form = _doc.getFieldAsString("Form").toLowerCase();
			if (form.equals("fo_vacation_request"))
				return VacationRequest.class;

			return null;
		}
	}

	static class VacationRequest extends DefaultDocument {

		protected VacationRequest(Database d, org.riverframework.module.Document _doc) {
			super(d, _doc);
		}

		@Override
		protected VacationRequest afterCreate() {
			setForm("fo_vacation_request");
			return this;
		}
	}

	@Test
	public void testCreateAndGetVacationRequest() {
		assertTrue("The test database could not be opened as a VacationDatabase.", vacationDatabase.isOpen());

		VacationRequest doc = vacationDatabase.createDocument(VacationRequest.class);

		assertTrue("There is a problem creating a new VacationRequest in the test database.", doc.isOpen());

		String universalId = doc
				.setField("TEST_FIELD", "YES")
				.setForm("TestForm")
				.save()
				.getObjectId();

		doc = null;
		doc = vacationDatabase.getDocument(VacationRequest.class, universalId);

		assertTrue("There is a problem getting a VacationRequest created in the test database.", doc.isOpen());
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		DocumentCollection col = null;
		col = database.getAllDocuments().deleteAll();

		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			database.createDocument(DefaultDocument.class)
					.setForm(TEST_FORM)
					.setField("Value", rs.nextString())
					.save();
		}

		database.createDocument(DefaultDocument.class)
				.setForm(TEST_FORM)
				.setField("Value", "THIS_IS_THE_DOC")
				.save();

		database.refreshSearchIndex();

		col = null;
		col = database.search("THIS IS IMPOSSIBLE TO FIND");
		assertTrue("The search returns values for a query that would returns nothing.", col.isEmpty());

		col = null;
		col = database.search("THIS_IS_THE_DOC");
		assertFalse("The search does not returns values for a query that would returns something.", col.isEmpty());
	}

	@Test
	public void testGetDocumentCollection() {
		assertTrue("The vacation database could not be instantiated.", vacationDatabase != null);
		assertTrue("The vacation database could not be opened.", vacationDatabase.isOpen());

		vacationDatabase.getAllDocuments().deleteAll();

		vacationDatabase.createDocument(VacationRequest.class)
				.setField("Requester", "John")
				.setField("Time", 30)
				.save();

		vacationDatabase.createDocument(VacationRequest.class)
				.setField("Requester", "Kathy")
				.setField("Time", 25)
				.save();

		vacationDatabase.createDocument(VacationRequest.class)
				.setField("Requester", "Michael")
				.setField("Time", 27)
				.save();

		DocumentCollection col = vacationDatabase.getAllDocuments();

		for (Document doc : col) {
			assertTrue("It could not possible load the vacation request object from the DocumentCollection.", doc.isOpen());
		}

		for (int i = 0; i < col.size(); i++) {
			Document v = col.get(i);
			assertTrue("It could not possible load the vacation request object from the DocumentCollection.", v.isOpen());
		}

		for (Document doc : col) {
			assertTrue("It could not possible load the vacation request object from the DocumentCollection.", doc.isOpen());
			assertTrue("The vacation request object from the DocumentCollection is an instance from " + doc.getClass().getName()
					+ ", and not from VacationRequest.",
					doc.getClass().getSimpleName().contains("VacationRequest"));
		}
	}

	static class Person extends DefaultDocument implements Document, Unique {

		protected Person(Database d, org.riverframework.module.Document doc) {
			super(d, doc);
		}

		public static String getIndexName() {
			return "vi_ap_people_index";
		}

		@Override
		public String getId() {
			return getFieldAsString("ca_pe_name");
		}

		@Override
		public org.riverframework.Document generateId() {
			// Do nothing
			return this;
		}

		@Override
		public org.riverframework.Document setId(String arg0) {
			setField("ca_pe_name", arg0);
			return this;
		}

	}

	@Test
	public void testGetDocument() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		database.createDocument(Person.class)
				.setId("John")
				.setForm("fo_ap_people")
				.setField("Age", 30)
				.save();

		database.createDocument(Person.class)
				.setId("Kathy")
				.setForm("fo_ap_people")
				.setField("Age", 25)
				.save();

		database.createDocument(Person.class)
				.setId("Jake")
				.setForm("fo_ap_people")
				.setField("Age", 27)
				.save();

		Document p = database.getDocument(Person.class, "Kathy");
		assertTrue("It could not possible load the person object for Kathy.", p.isOpen());
		assertTrue("It could not possible get the Kathy's age.", p.getFieldAsInteger("Age") == 25);

		String unid = p.getObjectId();
		p = null;
		p = database.getDocument(unid);
		assertTrue("It should be possible to load a person object for Kathy with its Universal Id.", p.isOpen());

		p = null;
		p = database.getDocument("Kathy");
		assertFalse("It should not be possible to load a person object for Kathy without indicate its class.", p.isOpen());
	}

	@Test
	public void testGetFields() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		database.createDocument(Person.class)
				.setId("Kathy")
				.setForm("fo_ap_people")
				.setField("Age", 25)
				.save();

		Document p = database.getDocument(Person.class, "Kathy");
		assertTrue("It could not possible load the person object for Kathy.", p.isOpen());

		Map<String, Vector<Object>> fields = p.getFields();

		assertTrue("It could not possible get the fields from the Kathy's document.", fields.size() > 0);

		Vector<Object> field = fields.get("ca_pe_name");
		String value = field.get(0).toString();

		assertTrue("It could not possible get the field Name from the Kathy's document.", value.equals("Kathy"));

		field = fields.get("Age");
		int age = ((Double) field.get(0)).intValue();

		assertTrue("It could not possible get the field Age from the Kathy's document.", age == 25);

	}

}
