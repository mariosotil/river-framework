package org.riverframework.wrapper;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentList;
import org.riverframework.wrapper.Session;

public abstract class AbstractDocumentListTest {
	final String TEST_FORM = "TestForm";

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

				session = (Session) context.getSession().getWrapperObject();
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
	public void testSize() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		DocumentList col = null;
		col = database.getAllDocuments().deleteAll();

		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			database.createDocument()
					.setField("Form", TEST_FORM)
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
		assertTrue("The search returns values for a query that would returns nothing.", col.size() == 0);

		col = null;
		col = database.search("THIS_IS_THE_DOC");
		assertTrue("The search does not returns values for a query that would returns something.", col.size() > 0);
	}

	@Test
	public void testDeleteAll() {
		assertTrue("The test database could not be opened.", database.isOpen());

		// Creating at least one document
		@SuppressWarnings("unused")
		Document doc = database.createDocument()
				.setField("Form", TEST_FORM)
				.save();

		DocumentList col = database.getAllDocuments();
		col.deleteAll();

		col = null;
		col = database.getAllDocuments();

		assertTrue("The test database still has documents.", col.size() == 0);
	}
}
