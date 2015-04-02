package org.riverframework.core;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentCollection;
import org.riverframework.RandomString;
import org.riverframework.Session;

public abstract class AbstractDocumentCollectionTest {
	protected Session session = null;
	protected Context context = null;
	protected Database database = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				String className = this.getClass().getPackage().getName() + ".Context";
				Class<?> clazz = Class.forName(className);
				if (org.riverframework.Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = context.getSession();
				database = session.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());
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

		DocumentCollection col = null;
		col = database.getAllDocuments().deleteAll();

		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			database.createDocument(DefaultDocument.class)
					.setField("Form", TEST_FORM)
					.setField("Value", rs.nextString())
					.save();
		}

		database.createDocument(DefaultDocument.class)
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
		Document rDoc = database
				.createDocument(DefaultDocument.class)
				.setField("Form", TEST_FORM)
				.save();

		DocumentCollection col = database.getAllDocuments();
		col.deleteAll();

		col = null;
		col = database.getAllDocuments();

		assertTrue("The test database still has documents.", col.size() == 0);
	}
}
