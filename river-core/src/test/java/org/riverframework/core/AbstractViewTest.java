package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentIterator;
import org.riverframework.RandomString;
import org.riverframework.Session;
import org.riverframework.View;

public abstract class AbstractViewTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";

	protected Session session = null;
	protected Context context = null;
	protected Database database = null;

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
	public void testGetDocumentAndRefreshView() throws InterruptedException {
		assertTrue("The test database could not be opened.", database.isOpen());

		RandomString rs = new RandomString(10);
		View view = database.getView(DefaultView.class, TEST_VIEW);

		assertTrue("The test view could not be created in the test database.", view.isOpen());

		Document doc = database.createDocument(DefaultDocument.class).setField("Form", TEST_FORM);

		String key = rs.nextString();

		doc.setField("TestKeyColumn1", key).save();
		view.refresh();

		doc = null;
		doc = view.getDocumentByKey(DefaultDocument.class, key);
		assertTrue("The test document could not be found in the view.", doc.isOpen());

		doc = null;
		doc = view.getDocumentByKey(DefaultDocument.class, "%%%%%");
		assertFalse("It was found an unexistent document in the view.", doc.isOpen());

		/*
		 * This code doesn't works because the created view does not works as
		 * expected. It's necessary to open the Designer and update the view to
		 * get it works.
		 * 
		 * String viewName = rs.nextString(); String formName = rs.nextString();
		 * org.riverframework.View rView = rDatabase.getView(viewName);
		 * if(!rView.isOpen()) { rView = rDatabase.createView(viewName,
		 * "Form =\"" + formName + "\""); }
		 * 
		 * assertTrue("The test view could not be created in the test database.",
		 * rView.isOpen());
		 * 
		 * String title = rs.nextString(); String field = rs.nextString();
		 * String formula = "@GetField({" + field + "})"; String key =
		 * rs.nextString();
		 * 
		 * rView.addColumn(0).modifyColumn(0, title, formula, true).refresh();
		 * org.riverframework.Document rDoc =
		 * rDatabase.createDocument(formName);
		 * 
		 * rDoc.setField(field, key); rDoc.save();
		 */
	}

	@Test
	public void testIsOpen() {
		assertTrue("The test database could not be opened.", database.isOpen());

		// RandomString rs = new RandomString(10);
		View view = database.getView(DefaultView.class, TEST_VIEW);

		assertTrue("The test view could not be found in the test database.", view.isOpen());

		view = null;
		view = database.getView(DefaultView.class, "%%%$%$%$%%$");

		assertFalse("An unexistant view could be found in the test database.", view.isOpen());
	}

	@Test
	public void testGetDocumentIterator() {
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		for (int i = 0; i < 100; i++) {
			database.createDocument(DefaultDocument.class).setField("Form", TEST_FORM).setField("Counter", i).save();
		}

		View view = database.getView(DefaultView.class, TEST_VIEW);
		view.refresh();
		DocumentIterator col = view.getAllDocuments();

		int size = 0;
		while (col.hasNext()) {
			col.next();
			size++;
		}

		assertTrue("There is a problem getting documents from the database.", size == 100);
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		for (int i = 0; i < 10; i++) {
			database.createDocument(DefaultDocument.class).setField("Form", TEST_FORM).setField("Text", "I_AM_THE_" + i).save();
		}

		database.refreshSearchIndex();

		DocumentIterator col = database.getView(DefaultView.class, TEST_VIEW).search("I_AM_THE_4");

		assertTrue("There is a problem finding documents from the database.", col.hasNext());

		Document doc = col.next();

		assertFalse("There is a problem finding documents from the database.", col.hasNext());
		assertTrue("There is a problem finding documents from the database.", doc.getFieldAsString("Text").equals("I_AM_THE_4"));
	}
}
