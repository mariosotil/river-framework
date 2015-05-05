package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
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
import org.riverframework.wrapper.View;

public abstract class AbstractViewTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";

	protected Session session = null;
	protected Database database = null;

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
	public void testGetDocumentAndRefreshView() throws InterruptedException {
		assertTrue("The test database could not be opened.", database.isOpen());

		RandomString rs = new RandomString(10);
		View rView = database.getView(TEST_VIEW);

		assertTrue("The test view could not be created in the test database.", rView.isOpen());

		Document doc = database.createDocument()
				.setField("Form", TEST_FORM);

		String key = rs.nextString();

		doc.setField("TestKeyColumn1", key)
				.save();
		rView.refresh();

		doc = null;
		doc = rView.getDocumentByKey(key);
		assertTrue("The test document could not be found in the view.", doc.isOpen());

		doc = null;
		doc = rView.getDocumentByKey("%%%%%");
		assertFalse("It was found an unexistent document in the view.", doc.isOpen());

		/*
		 * This code doesn't works because the created view does not works
		 * as expected. It's necessary to open the Designer and update the
		 * view to get it work.
		 * 
		 * String viewName = rs.nextString();
		 * String formName = rs.nextString();
		 * org.riverframework.View rView = rDatabase.getView(viewName);
		 * if(!rView.isOpen()) {
		 * rView = rDatabase.createView(viewName, "Form =\"" + formName + "\"");
		 * }
		 * 
		 * assertTrue("The test view could not be created in the test database.", rView.isOpen());
		 * 
		 * String title = rs.nextString();
		 * String field = rs.nextString();
		 * String formula = "@GetField({" + field + "})";
		 * String key = rs.nextString();
		 * 
		 * rView.addColumn(0).modifyColumn(0, title, formula, true).refresh();
		 * org.riverframework.Document rDoc = rDatabase.createDocument(formName);
		 * 
		 * rDoc.setField(field, key);
		 * rDoc.save();
		 */
	}

	@Test
	public void testIsOpen() {
		assertTrue("The test database could not be opened.", database.isOpen());

		// RandomString rs = new RandomString(10);
		View view = database.getView(TEST_VIEW);

		assertTrue("The test view could not be found in the test database.", view.isOpen());

		view = null;
		view = database.getView("%%%$%$%$%%$");

		assertFalse("An unexistant view could be found in the test database.", view.isOpen());
	}

	/*
	 * @Test
	 * public void testModifyColumn() {
	 * assertTrue("The test database could not be opened.", rDatabase.isOpen());
	 * 
	 * RandomString rs = new RandomString(10);
	 * org.riverframework.View rView = rDatabase.createView(rs.nextString(), "Select @All");
	 * 
	 * assertTrue("The test view could not be created in the test database.", rView.isOpen());
	 * 
	 * String title = rs.nextString();
	 * String formula = rs.nextString();
	 * 
	 * rView.modifyColumn(0, title, formula, true);
	 * 
	 * assertTrue("Could not be modified the title in the first column of the test view.",
	 * title.equals(rView.getColumnTitle(0)));
	 * assertTrue("Could not be modified the formula in the first column of the test view.",
	 * formula.equals(rView.getColumnFormula(0)));
	 * assertTrue("Could not be modified if it is sorted in the first column of the test view.",
	 * rView.isColumnSorted(0));
	 * }
	 */

	@Test
	public void testGetDocumentList() {
		assertTrue("The test database could not be opened.", database.isOpen());

		// database.getAllDocuments().deleteAll();

		View view = database.getView(TEST_VIEW);
		view.refresh();
		DocumentList col = view.getAllDocuments().deleteAll();

		assertTrue("There is a problem with the database. There are still documents after the delete.", col.size() == 0);

		for (int i = 0; i < 10; i++) {
			database.createDocument()
					.setField("Form", TEST_FORM)
					.setField("Counter", i)
					.save();
		}

		view.refresh();
		col = view.getAllDocuments();

		assertTrue("There is a problem getting documents from the database.", col.size() == 10);
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		for (int i = 0; i < 10; i++) {
			database.createDocument()
					.setField("Form", TEST_FORM)
					.setField("Text", "I_AM_THE_" + i)
					.save();
		}

		database.refreshSearchIndex();

		DocumentList col = database
				.getView(TEST_VIEW)
				.search("I_AM_THE_4");

		assertTrue("There is a problem finding documents from the database.", col.size() == 1);
		assertTrue("There is a problem finding documents from the database.",
				col.get(0).getFieldAsString("Text").equals("I_AM_THE_4"));
	}
}
