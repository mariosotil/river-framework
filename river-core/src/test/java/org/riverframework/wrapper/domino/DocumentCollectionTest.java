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

public class DocumentCollectionTest {
	final String TEST_FORM = "TestForm";

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
	public void testSize() {
		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		DocumentCollection col = null;
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

		DocumentCollection col = database.getAllDocuments();
		col.deleteAll();

		col = null;
		col = database.getAllDocuments();

		assertTrue("The test database still has documents.", col.size() == 0);
	}
}
