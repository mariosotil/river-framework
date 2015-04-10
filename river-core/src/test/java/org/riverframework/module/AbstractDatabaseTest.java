package org.riverframework.module;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;
import org.riverframework.module.Database;
import org.riverframework.module.Document;
import org.riverframework.module.DocumentList;
import org.riverframework.module.Session;
import org.riverframework.module.View;

public abstract class AbstractDatabaseTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	protected Session session = null;
	protected Database database = null;

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

				session = (Session) context.getSession().getModuleObject();
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
	public void testGetView() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		View view = database.getView(TEST_VIEW);

		assertTrue("There is a problem getting the view created in the test database.", view.isOpen());
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		DocumentList col = null;

		database.getAllDocuments().deleteAll();
		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			database.createDocument()
					.setField("Value", rs.nextString())
					.save();
		}

		database.createDocument()
				.setField("Form", TEST_FORM)
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
	public void testGetDocument() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		database.createDocument()
				.setField("Id", "John")
				.setField("Form", "fo_ap_people")
				.setField("Age", 30)
				.save();

		database.createDocument()
				.setField("Id", "Kathy")
				.setField("Form", "fo_ap_people")
				.setField("Age", 25)
				.save();

		Document doc = database.createDocument()
				.setField("Id", "Jake")
				.setField("Form", "fo_ap_people")
				.setField("Age", 27)
				.save();

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

		database.createDocument()
				.setField("Requester", "John")
				.setField("Time", 30)
				.save();

		database.createDocument()
				.setField("Requester", "Kathy")
				.setField("Time", 25)
				.save();

		database.createDocument()
				.setField("Requester", "Michael")
				.setField("Time", 27)
				.save();

		DocumentList col = database.getAllDocuments();

		for (Document doc : col) {
			assertTrue("It could not possible load the vacation request object from the DocumentList.", doc.isOpen());
		}

		for (int i = 0; i < col.size(); i++) {
			Document v = col.get(i);
			assertTrue("It could not possible load the Document object from the DocumentList.", v.isOpen());
		}

		for (Document doc : col) {
			assertTrue("It could not possible load the Document object from the DocumentList.", doc.isOpen());
		}
	}
}
