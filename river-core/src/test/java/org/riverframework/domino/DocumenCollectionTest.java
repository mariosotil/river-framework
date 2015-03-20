package org.riverframework.domino;

import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;
import org.riverframework.domino.Database;
import org.riverframework.domino.DefaultDatabase;
import org.riverframework.domino.DefaultDocument;
import org.riverframework.domino.DefaultDocumentCollection;
import org.riverframework.domino.DefaultSession;
import org.riverframework.domino.Document;
import org.riverframework.domino.DocumentCollection;
import org.riverframework.domino.Session;

public class DocumenCollectionTest {
	private Session session = DefaultSession.getInstance();
	private Database rDatabase = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void init() {
		NotesThread.sinitThread();

		try {
			session.open(LocalContext.getPassword());
			rDatabase = session.getDatabase(DefaultDatabase.class, "", LocalContext.getDatabase());
		} catch (Exception e) {
			throw new RiverException(e);
		}
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

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		org.riverframework.domino.DefaultDocumentCollection rIterator = (DefaultDocumentCollection) rDatabase.getAllDocuments();
		rIterator.remove();
	}

}
