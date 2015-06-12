package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;

public abstract class AbstractDatabaseTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	protected Session<?> session = null;
	protected Database<?> database = null;

	protected Context context = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				Class<?> clazz = Class.forName(this.getClass().getPackage().getName() + ".Context");
				if (Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = (Session<?>) context.getSession().getWrapperObject();
				database = session.getDatabase(context.getTestDatabaseServer(), context.getTestDatabasePath());
				database.getAllDocuments().deleteAll();
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

	@Test
	public void testCreateAndGetView() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View<?> view = database.createView(name, "SELECT Form = \"" + form + "\"");

		assertTrue("There is a problem creating the view in the test database.", view.isOpen());

		view.close();
		view = null;

		int i = 0;
		for (i = 0; i < 10; i++) {
			database.createDocument().setField("Form", form).setField("Value", i).save();
		}

		view = database.getView(name);
		assertTrue("There is a problem opening the last view created in the test database.", view.isOpen());

		DocumentIterator<?> it = view.getAllDocuments();

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
			Document<?> doc = it.next();
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

		DocumentIterator<?> col = null;

		database.getAllDocuments().deleteAll();
		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			database.createDocument().setField("Value", rs.nextString()).save();
		}

		database.createDocument().setField("Form", TEST_FORM).setField("Value", "THIS_IS_THE_DOC").save();

		database.refreshSearchIndex();

		col = null;
		col = database.search("THIS IS IMPOSSIBLE TO FIND");
		assertFalse("The search returns values for a query that would returns nothing.", col.hasNext());

		col = null;
		col = database.search("THIS_IS_THE_DOC");
		assertTrue("The search does not returns values for a query that would returns something.", col.hasNext());
	}

	@Test
	public void testGetDocument() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		database.createDocument().setField("Id", "John").setField("Form", "fo_ap_people").setField("Age", 30).save();

		database.createDocument().setField("Id", "Kathy").setField("Form", "fo_ap_people").setField("Age", 25).save();

		Document<?> doc = database.createDocument().setField("Id", "Jake").setField("Form", "fo_ap_people").setField("Age", 27).save();

		String unid = doc.getObjectId();
		doc = null;
		doc = database.getDocument(unid);
		assertTrue("It should be possible to load a person object with its Universal Id.", doc.isOpen());
	}

	@Test
	public void testGetDocumentCollection() {
		assertTrue("The database could not be instantiated.", database != null);
		assertTrue("The database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		database.createDocument().setField("Requester", "John").setField("Time", 30).save();

		database.createDocument().setField("Requester", "Kathy").setField("Time", 25).save();

		database.createDocument().setField("Requester", "Michael").setField("Time", 27).save();

		DocumentIterator<?> col = database.getAllDocuments();

		for (Document<?> doc : col) {
			assertTrue("It could not possible load the vacation request object from the DocumentList.", doc.isOpen());
		}
	}
}
