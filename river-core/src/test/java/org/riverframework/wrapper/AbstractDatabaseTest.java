package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;

//import java.util.List;

public abstract class AbstractDatabaseTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	protected Session<?> _session = null;
	protected Database<?> _database = null;

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

				_session = context.getSession().getWrapperObject();
				_database = _session.getDatabase(context.getTestDatabasePath());
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
	public void testOpenedDatabase() {
		assertTrue("The test database could not be opened.", _database.isOpen());
		assertFalse("The file path could not be detected.", _database.getFilePath()
																		.equals(""));
		assertFalse("The database name could not be detected.", _database.getName()
																			.equals(""));
	}

	@Test
	public void testCreateAndGetView() {
		assertTrue("The test database could not be instantiated.", _database != null);
		assertTrue("The test database could not be opened.", _database.isOpen());

		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View<?> _view = _database.createView(name, "SELECT Form = \"" + form + "\"");
		// _view.addColumn("id", "Id", true);

		assertTrue("There is a problem creating the view in the test database.", _view.isOpen());

		_view = null;
		for (i = 0; i < 10; i++) {
			_database.createDocument()
						.setField("Form", form)
						.setField("Id", String.valueOf(10 - i))
						.setField("Value", i + 1)
						.save();
		}

		_view = _database.getView(name);
		assertTrue("There is a problem opening the last view created in the test database.", _view.isOpen());

		_view.refresh();

		for (i = 0; i < 10; i++) {
			Document<?> _doc = _view.getDocumentByKey(String.valueOf(i + 1));
			assertTrue("There is a problem retrieving the Value field from the document with id '" + (i + 1) + "'", _doc.getFieldAsInteger("Value") == 10 - i);
		}

		i = 0;
		DocumentIterator<?, ?> _it = _view.getAllDocuments();
		while (_it.hasNext()) {
			i++;
			_it.next();
		}
		assertTrue("There is a problem with the documents indexed in the last view.", i == 10);

		i = 0;
		_it = _view.getAllDocuments();
		while (_it.hasNext()) {
			i++;
			Document<?> _doc = _it.next();
			_doc.delete();
		}

		_it = _view.getAllDocuments();
		assertFalse("There is a problem with the last documents created when we try to delete them.", _it.hasNext());

		_view.delete();

		assertFalse("There is a problem deleting the last view created.", _view.isOpen());
	}

	@Test
	public void testSearch() {
		assertTrue("The test database could not be instantiated.", _database != null);
		assertTrue("The test database could not be opened.", _database.isOpen());

		DocumentIterator<?, ?> _iterator = null;

		_database.getAllDocuments()
					.deleteAll();
		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			_database.createDocument()
						.setField("Value", rs.nextString())
						.save();
		}

		_database.createDocument()
					.setField("Form", TEST_FORM)
					.setField("Value", "THIS_IS_THE_DOC")
					.save();

		_database.refreshSearchIndex(true);

		_iterator = null;
		_iterator = _database.search("Value=\"THIS IS IMPOSSIBLE TO FIND\"");
		assertFalse("The search returns values for a query that would returns nothing.", _iterator.hasNext());

		_iterator = null;
		_iterator = _database.search("Value=\"THIS_IS_THE_DOC\"");
		assertTrue("The search does not returns values for a query that would returns something.", _iterator.hasNext());
	}

	@Test
	public void testGetDocument() {
		assertTrue("The test database could not be instantiated.", _database != null);
		assertTrue("The test database could not be opened.", _database.isOpen());

		_database.getAllDocuments()
					.deleteAll();

		_database.createDocument()
					.setField("Id", "John")
					.setField("Form", "fo_ap_people")
					.setField("Age", 30)
					.save();

		_database.createDocument()
					.setField("Id", "Kathy")
					.setField("Form", "fo_ap_people")
					.setField("Age", 25)
					.save();

		Document<?> doc = _database.createDocument()
									.setField("Id", "Jake")
									.setField("Form", "fo_ap_people")
									.setField("Age", 27)
									.save();

		String objectId = doc.getObjectId();
		doc = null;
		doc = _database.getDocument(objectId);
		assertTrue("It should be possible to load a person object with its Object Id.", doc.isOpen());
	}

	@Test
	public void testGetDocumentCollection() {
		assertTrue("The database could not be instantiated.", _database != null);
		assertTrue("The database could not be opened.", _database.isOpen());

		_database.getAllDocuments()
					.deleteAll();

		_database.createDocument()
					.setField("Requester", "John")
					.setField("Time", 30)
					.save();

		_database.createDocument()
					.setField("Requester", "Kathy")
					.setField("Time", 25)
					.save();

		_database.createDocument()
					.setField("Requester", "Michael")
					.setField("Time", 27)
					.save();

		DocumentIterator<?, ?> _iterator = _database.getAllDocuments();

		for (Document<?> doc : _iterator) {
			assertTrue("It could not possible load the vacation request object from the DocumentList.", doc.isOpen());
		}
	}
}
