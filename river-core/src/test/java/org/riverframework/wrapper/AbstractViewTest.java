package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;
import org.riverframework.RiverException;

public abstract class AbstractViewTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";

	protected Session<?> _session = null;
	protected Database<?> _database = null;

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

				_session = context.getSession().getWrapperObject();
				_database = _session.getDatabase(context.getTestDatabaseServer(), context.getTestDatabasePath());
				_database.getAllDocuments().deleteAll();

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
		assertTrue("The test database could not be opened.", _database.isOpen());

		RandomString rs = new RandomString(10);
		View<?> _view = _database.getView(TEST_VIEW);

		assertTrue("The test view could not be created in the test database.", _view.isOpen());

		Document<?> doc = _database.createDocument().setField("Form", TEST_FORM);

		String key = rs.nextString();

		doc.setField("TestKeyColumn1", key).save();
		_view.refresh();

		doc = null;
		doc = _view.getDocumentByKey(key);
		assertTrue("The test document could not be found in the view.", doc.isOpen());

		doc = null;
		doc = _view.getDocumentByKey("%%%%%");
		assertFalse("It was found an unexistent document in the view.", doc.isOpen());
	}

	@Test
	public void testIsOpen() {
		assertTrue("The test database could not be opened.", _database.isOpen());

		// RandomString rs = new RandomString(10);
		View<?> _view = _database.getView(TEST_VIEW);

		assertTrue("The test view could not be found in the test database.", _view.isOpen());

		_view = null;
		_view = _database.getView("%%%$%$%$%%$");

		assertFalse("An unexistant view could be found in the test database.", _view.isOpen());
	}

	@Test
	public void testGetDocumentIterator() {
		assertTrue("The test database could not be opened.", _database.isOpen());

		// database.getAllDocuments().deleteAll();

		View<?> _view = _database.getView(TEST_VIEW);
		_view.refresh();
		DocumentIterator<?, ?> _it = _view.getAllDocuments().deleteAll();

		assertFalse("There is a problem with the database. There are still documents after the delete.", _it.hasNext());

		for (int i = 0; i < 10; i++) {
			_database.createDocument().setField("Form", TEST_FORM).setField("Counter", i).save();
		}

		_view.refresh();
		_it = _view.getAllDocuments();

		int size = 0;
		while (_it.hasNext()) {
			_it.next();
			size++;
		}
		assertTrue("There is a problem getting documents from the database.", size == 10);
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be opened.", _database.isOpen());

		_database.getAllDocuments().deleteAll();

		for (int i = 0; i < 10; i++) {
			_database.createDocument().setField("Form", TEST_FORM).setField("Text", "I_AM_THE_" + i).save();
		}

		_database.refreshSearchIndex(true);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			throw new RiverException(e);
		}

		DocumentIterator<?, ?> _it = _database.getView(TEST_VIEW).search("I_AM_THE_4");
		assertTrue("There is a problem finding documents from the database.", _it.hasNext());
		Document<?> _doc = _it.next();
		assertTrue("There is a problem finding documents from the database.",
				_doc.getFieldAsString("Text").equals("I_AM_THE_4"));
	}
}
