package org.riverframework.core;

import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentCollection;
import org.riverframework.RandomString;
import org.riverframework.Session;
import org.riverframework.core.DefaultDatabase;
import org.riverframework.core.DefaultDocument;
import org.riverframework.core.DefaultSession;

public class DocumentCollectionTest {
	private Session session = DefaultSession.getInstance();
	private Context context = Context.getInstance();
	private Database rDatabase = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void init() {
		NotesThread.sinitThread();

		session.open(context.getSession());
		rDatabase = session.getDatabase(DefaultDatabase.class, "", context.getDatabase());
		rDatabase.getAllDocuments().deleteAll();
	}

	@After
	public void close() {
		session.close();
		NotesThread.stermThread();
	}

	@Test
	public void testSize() {
		assertTrue("The test database could not be instantiated.", rDatabase != null);
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DocumentCollection col = null;
		col = rDatabase.getAllDocuments().deleteAll();

		RandomString rs = new RandomString(10);

		for (int i = 0; i < 10; i++) {
			rDatabase.createDocument(DefaultDocument.class)
					.setForm(TEST_FORM)
					.setField("Value", rs.nextString())
					.save();
		}

		rDatabase.createDocument(DefaultDocument.class)
				.setForm(TEST_FORM)
				.setField("Value", "THIS_IS_THE_DOC")
				.save();

		rDatabase.refreshSearchIndex();

		col = null;
		col = rDatabase.search("THIS IS IMPOSSIBLE TO FIND");
		assertTrue("The search returns values for a query that would returns nothing.", col.size() == 0);

		col = null;
		col = rDatabase.search("THIS_IS_THE_DOC");
		assertTrue("The search does not returns values for a query that would returns something.", col.size() > 0);
	}

	@Test
	public void testDeleteAll() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		// Creating at least one document
		@SuppressWarnings("unused")
		Document rDoc = rDatabase
				.createDocument(DefaultDocument.class)
				.setForm(TEST_FORM)
				.save();

		DocumentCollection col = rDatabase.getAllDocuments();
		col.deleteAll();

		col = null;
		col = rDatabase.getAllDocuments();

		assertTrue("The test database still has documents.", col.size() == 0);
	}
}
