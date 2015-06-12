package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;

public abstract class AbstractViewTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";

	protected Session<?> session = null;
	protected Database<?> database = null;

	protected Context context = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				String className = this.getClass().getPackage().getName() + ".Context";
				Class<?> clazz = Class.forName(className);
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
	public void testGetDocumentAndRefreshView() throws InterruptedException {
		assertTrue("The test database could not be opened.", database.isOpen());

		RandomString rs = new RandomString(10);
		View<?> rView = database.getView(TEST_VIEW);

		assertTrue("The test view could not be created in the test database.", rView.isOpen());

		Document<?> doc = database.createDocument().setField("Form", TEST_FORM);

		String key = rs.nextString();

		doc.setField("TestKeyColumn1", key).save();
		rView.refresh();

		doc = null;
		doc = rView.getDocumentByKey(key);
		assertTrue("The test document could not be found in the view.", doc.isOpen());

		doc = null;
		doc = rView.getDocumentByKey("%%%%%");
		assertFalse("It was found an unexistent document in the view.", doc.isOpen());
	}

	@Test
	public void testIsOpen() {
		assertTrue("The test database could not be opened.", database.isOpen());

		// RandomString rs = new RandomString(10);
		View<?> view = database.getView(TEST_VIEW);

		assertTrue("The test view could not be found in the test database.", view.isOpen());

		view = null;
		view = database.getView("%%%$%$%$%%$");

		assertFalse("An unexistant view could be found in the test database.", view.isOpen());
	}

	@Test
	public void testGetDocumentIterator() {
		assertTrue("The test database could not be opened.", database.isOpen());

		// database.getAllDocuments().deleteAll();

		View<?> view = database.getView(TEST_VIEW);
		view.refresh();
		DocumentIterator<?> col = view.getAllDocuments().deleteAll();

		assertFalse("There is a problem with the database. There are still documents after the delete.", col.hasNext());

		for (int i = 0; i < 10; i++) {
			database.createDocument().setField("Form", TEST_FORM).setField("Counter", i).save();
		}

		view.refresh();
		col = view.getAllDocuments();

		int size = 0;
		while (col.hasNext()) {
			col.next();
			size++;
		}
		assertTrue("There is a problem getting documents from the database.", size == 10);
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		for (int i = 0; i < 10; i++) {
			database.createDocument().setField("Form", TEST_FORM).setField("Text", "I_AM_THE_" + i).save();
		}

		database.refreshSearchIndex();

		DocumentIterator<?> col = database.getView(TEST_VIEW).search("I_AM_THE_4");
		Document<?> doc = col.next();
		assertFalse("There is a problem finding documents from the database.", col.hasNext());
		assertTrue("There is a problem finding documents from the database.", doc.getFieldAsString("Text").equals("I_AM_THE_4"));
	}
}
