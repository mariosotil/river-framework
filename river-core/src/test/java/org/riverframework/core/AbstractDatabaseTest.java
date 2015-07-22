package org.riverframework.core;

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

public abstract class AbstractDatabaseTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	static class VacationDatabase extends AbstractIndexedDatabase<VacationDatabase> {

		protected VacationDatabase(Session session, org.riverframework.wrapper.Database<?> _database) {
			super(session, _database);
		}

		@Override
		public Class<? extends AbstractDocument<?>> getClassFromDocument(org.riverframework.wrapper.Document<?> _doc) {
			if (_doc.getFieldAsString("Form").equals("fo_vacation_request"))
				return VacationRequest.class;

			return DefaultDocument.class;
		}

		@Override
		protected VacationDatabase getThis() {
			return this;
		}
	}

	static class VacationRequest extends AbstractDocument<VacationRequest> {

		protected VacationRequest(Database database, org.riverframework.wrapper.Document<?> _doc) {
			super(database, _doc);
		}

		@Override
		public VacationRequest afterCreate() {
			setField("Form", "fo_vacation_request").setField("TEST_FIELD", "YES");

			return getThis();
		}

		@Override
		protected VacationRequest getThis() {
			return this;
		}
	}

	static class Person extends AbstractIndexedDocument<Person> {

		protected Person(Database database, org.riverframework.wrapper.Document<?> _doc) {
			super(database, _doc);

			indexName = new String[] {"vi_ap_people_index"};
			indexField = "ca_pe_name";
		}

		@Override 
		public Person afterCreate() {
			return setField("Form", "fo_ap_people");			
		}

		@Override
		public Person generateId() {
			// Do nothing
			return this;
		}

		@Override
		protected Person getThis() {
			return this;
		}

	}

	protected Session session = null;
	protected Database database = null;
	protected Context context = null;
	protected IndexedDatabase vacationDatabase = null;

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
				database = session.getDatabase(context.getTestDatabaseServer(), context.getTestDatabasePath());
				database.getAllDocuments().deleteAll();

				vacationDatabase = session.getDatabase(VacationDatabase.class, context.getTestDatabaseServer(),
						context.getTestDatabasePath());
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
	// org.riverframework.development.Relation rRelation =
	// rDatabase.createRelation(TEST_GRAPH);
	//
	// assertTrue("There is a problem creating a new graph in the test database.",
	// rRelation.isOpen());
	//
	// String universalId =
	// rRelation.save(org.riverframework.Document.FORCE_SAVE).getUniversalId();
	// rRelation = null;
	//
	// rRelation = rDatabase.getRelation(universalId);
	//
	// assertTrue("There is a problem getting the graph created in the test database.",
	// rRelation.isOpen());
	// }

	@Test
	public void testCreateAndGetView() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View view = database.createView(name, "SELECT Form = \"" + form + "\"");

		assertTrue("There is a problem creating the view in the test database.", view.isOpen());

		view.close();
		view = null;

		int i = 0;
		for (i = 0; i < 10; i++) {
			database.createDocument().setField("Form", form).setField("Value", i).save();
		}

		view = database.getView(name);
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

	@Test
	public void testSearch() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		DocumentIterator col = database.getAllDocuments().deleteAll();

		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			database.createDocument().setField("Form", TEST_FORM).setField("Value", rs.nextString()).save();
		}

		database.createDocument().setField("Form", TEST_FORM).setField("Value", "THIS_IS_THE_DOC").save();

		database.refreshSearchIndex(true);

		col = null;
		col = database.search("THIS IS IMPOSSIBLE TO FIND");
		assertTrue("The search returns values for a query that would returns nothing.", !col.hasNext());

		col = null;
		col = database.search("THIS_IS_THE_DOC");
		assertTrue("The search does not returns values for a query that would returns something.", col.hasNext());
	}

	@Test
	public void testGetDocument() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		DocumentIterator iterator = database.getAllDocuments().deleteAll();
		iterator = database.getAllDocuments();
		assertFalse("The database still has documents.", iterator.hasNext());

		String unidJohn = database.createDocument().setField("Name", "John")
				.setField("Age", 30).save().getObjectId();

		String unidKathy = database.createDocument().setField("Name", "Kathy")
				.setField("Age", 25).save().getObjectId();

		String unidJake = database.createDocument().setField("Name", "Jake")
				.setField("Age", 27).save().getObjectId();

		boolean temp;

		Document p = database.getDocument(unidJake);
		temp = p.isOpen();
		assertTrue("It could not possible load the person object for Jake.", temp);
		temp = p.getFieldAsInteger("Age") == 27;
		assertTrue("It could not possible get the Jake's age.", temp);

		p = database.getDocument(unidJohn);
		temp = p.isOpen();
		assertTrue("It could not possible load the person object for John.", temp);
		temp = p.getFieldAsInteger("Age") == 30;
		assertTrue("It could not possible get the John's age.", temp);

		p = database.getDocument(unidKathy);
		temp = p.isOpen();
		assertTrue("It could not possible load the person object for Kathy.", temp);
		temp = p.getFieldAsInteger("Age") == 25;
		assertTrue("It could not possible get the Kathy's age.", temp);

		p = null;
		p = database.getDocument("%%%%%%");
		assertFalse("It should not be possible to load a person object for an unknown id.", p.isOpen());
	}

	@Test
	public void testGetFields() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		String id = database.createDocument().setField("ca_pe_name", "Kathy").setField("Form", "fo_ap_people")
				.setField("Age", 25).save().getObjectId();

		Document p = database.getDocument(id);
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

	@Test
	public void testOpenedExtendedDatabase() {
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());
		assertFalse("The file path could not be detected.", vacationDatabase.getFilePath().equals(""));
		assertFalse("The database name could not be detected.", vacationDatabase.getName().equals(""));
	}

	@Test
	public void testCreateAndGetExtendedView() {
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

	@Test
	public void testCreateAndGetVacationRequest() {
		assertTrue("The test database could not be opened as a VacationDatabase.", vacationDatabase.isOpen());

		VacationRequest doc = vacationDatabase.createDocument(VacationRequest.class);

		assertTrue("There is a problem creating a new VacationRequest in the test database.", doc.isOpen());

		String objectId = doc.save().getObjectId();

		doc = null;
		doc = vacationDatabase.getDocument(VacationRequest.class, objectId);

		assertTrue("There is a problem getting a VacationRequest created in the test database.", doc.isOpen());
	}

	@Test
	public void testExtendedSearch() {
		assertTrue("The test database could not be instantiated.", vacationDatabase != null);
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());

		DocumentIterator col = vacationDatabase.getAllDocuments().deleteAll();

		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			vacationDatabase.createDocument().setField("Form", TEST_FORM).setField("Value", rs.nextString()).save();
		}

		vacationDatabase.createDocument().setField("Form", TEST_FORM).setField("Value", "THIS_IS_THE_DOC").save();

		vacationDatabase.refreshSearchIndex(true);

		col = null;
		col = vacationDatabase.search("THIS IS IMPOSSIBLE TO FIND");
		assertTrue("The search returns values for a query that would returns nothing.", !col.hasNext());

		col = null;
		col = vacationDatabase.search("THIS_IS_THE_DOC");
		assertTrue("The search does not returns values for a query that would returns something.", col.hasNext());
	}

	@Test
	public void testGetExtendedDocumentIterator() {
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

		DocumentIterator it = vacationDatabase.getAllDocuments();

		for (Document doc : it) {
			assertTrue("It could not possible load the vacation request object from the DocumentIterator.",
					doc.isOpen());

			// Map<String, Field> fields = doc.getFields();
			// System.out.println(fields.size());
			assertTrue("The vacation request object from the DocumentIterator is an instance from "
					+ doc.getClass().getName() + ", and not from VacationRequest.", doc.getClass().getSimpleName()
					.contains("VacationRequest"));
		}
	}

	@Test
	public void testGetExtendedDocument() {
		assertTrue("The test database could not be instantiated.", vacationDatabase != null);
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());

		vacationDatabase.getAllDocuments().deleteAll();

		assertFalse("The database still has documents.", vacationDatabase.getAllDocuments().hasNext());

		vacationDatabase.createDocument(Person.class)
		.setId("John")
		.setField("Age", 30)
		.save();

		vacationDatabase.createDocument(Person.class)
		.setId("Jake")
		.setField("Age", 27)
		.save();

		vacationDatabase.createDocument(Person.class)
		.setId("Kathy")
		.setField("Age", 25)
		.save();

		Person p = vacationDatabase.getDocument(Person.class, "Kathy");
		assertTrue("It could not possible load the person object for Kathy.", p.isOpen());
		assertTrue("It could not possible get the Kathy's age.", p.getFieldAsInteger("Age") == 25);

		p = vacationDatabase.getDocument(Person.class, "Jake");
		assertTrue("It could not possible load the person object for Jake.", p.isOpen());
		assertTrue("It could not possible get the Jake's age.", p.getFieldAsInteger("Age") == 27);

		p = vacationDatabase.getDocument(Person.class, "John");
		assertTrue("It could not possible load the person object for John.", p.isOpen());
		assertTrue("It could not possible get the John's age.", p.getFieldAsInteger("Age") == 30);

		String unid = p.getObjectId();
		p = null;
		p = vacationDatabase.getDocument(Person.class, unid);
		assertTrue("It should be possible to load a person object for Kathy with its Universal Id.", p.isOpen());

		p = null;
		Document d = vacationDatabase.getDocument(DefaultDocument.class, "Kathy");
		assertFalse("It should not be possible to load a person object for Kathy without indicate its class.",
				d.isOpen());

	}

	@Test
	public void testGetExtendedFields() {
		assertTrue("The test database could not be instantiated.", vacationDatabase != null);
		assertTrue("The test database could not be opened.", vacationDatabase.isOpen());

		vacationDatabase.getAllDocuments().deleteAll();

		vacationDatabase.createDocument(Person.class).setId("Kathy").setField("Form", "fo_ap_people")
		.setField("Age", 25).save().close();

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
