package org.riverframework.domino;

import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DocumentCollectionTest {
	private Session session = DefaultSession.getInstance();
	private Database rDatabase = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void init() {
		NotesThread.sinitThread();

		session.open(Credentials.getPassword());
		rDatabase = session.getDatabase(DefaultDatabase.class, "", Context.getDatabase());
		rDatabase.getAllDocuments().removeAll();
	}

	@After
	public void close() {
		session.close();
		NotesThread.stermThread();
	}

	@Test
	public void testIteration() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		Document rDoc = rDatabase
				.createDocument(DefaultDocument.class)
				.setForm(TEST_FORM)
				.save();

		DocumentCollection rIterator = rDatabase.getAllDocuments();

		rDoc = null;

		while (rIterator.hasNext()) {
			rDoc = rIterator.next();
		}

		assertTrue("There is a problem getting documents from the database.", rDoc != null && rDoc.isOpen());
	}

	@Test
	public void testSize() {
		assertTrue("The test database could not be instantiated.", rDatabase != null);
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DocumentCollection col = null;
		col = rDatabase.getAllDocuments().removeAll();

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

		col = null;
		col = rDatabase.search("THIS IS IMPOSSIBLE TO FIND");
		assertTrue("The search returns values for a query that would returns nothing.", col.size() == 0);

		col = null;
		col = rDatabase.search("THIS_IS_THE_DOC");
		assertTrue("The search does not returns values for a query that would returns something.", col.size() > 0);

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		org.riverframework.domino.DefaultDocumentCollection rIterator = (DefaultDocumentCollection) rDatabase.getAllDocuments();
		rIterator.remove();
	}

	@Test
	public void testRemoveAll() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		// Creating at least one document
		@SuppressWarnings("unused")
		Document rDoc = rDatabase
				.createDocument(DefaultDocument.class)
				.setForm(TEST_FORM)
				.save();

		DocumentCollection col = rDatabase.getAllDocuments();
		col.removeAll();

		col = null;
		col = rDatabase.getAllDocuments();

		assertTrue("The test database still has documents.", col.size() == 0);
	}
}
