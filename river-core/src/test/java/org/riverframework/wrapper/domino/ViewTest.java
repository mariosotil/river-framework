package org.riverframework.wrapper.domino;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RandomString;
import org.riverframework.core.Credentials;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentCollection;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

public class ViewTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";

	private Session session = null;
	private Database database = null;

	@Before
	public void init() {
		NotesThread.sinitThread();

		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		session = new DefaultSession((String) null, (String) null, password);

		database = session.getDatabase("", Context.getDatabase());
		database.getAllDocuments().deleteAll();
	}

	@After
	public void close() {
		session.close();
		NotesThread.stermThread();
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
	public void testGetDocumentCollection() {
		assertTrue("The test database could not be opened.", database.isOpen());

		database.getAllDocuments().deleteAll();

		for (int i = 0; i < 10; i++) {
			database.createDocument()
					.setField("Form", TEST_FORM)
					.setField("Counter", i)
					.save();
		}

		View view = database.getView(TEST_VIEW);
		DocumentCollection col = view.getAllDocuments();

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

		DocumentCollection col = database
				.getView(TEST_VIEW)
				.search("I_AM_THE_4");

		assertTrue("There is a problem finding documents from the database.", col.size() == 1);
		assertTrue("There is a problem finding documents from the database.",
				col.get(0).getFieldAsString("Text").equals("I_AM_THE_4"));
	}
}
