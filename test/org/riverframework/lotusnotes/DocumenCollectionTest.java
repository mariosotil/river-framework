package org.riverframework.lotusnotes;

import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;

public class DocumenCollectionTest {
	final DefaultSession session = DefaultSession.getInstance();
	private DefaultDatabase rDatabase = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void init() {
		try {
			NotesThread.sinitThread();
			session.open(Credentials.getServer(), Credentials.getUser(), Credentials.getPassword());
			rDatabase = session.getDatabase(DefaultDatabase.class, "server1/wtres", "trabajo\\FW\\Beach.nsf");
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
		// assertFalse("The database Beach.nsf have isDocumentFactoryStrict = true.",
		// rDatabase.isDocumentFactoryStrict());

		DefaultDocument rDoc = rDatabase
				.createDocument(DefaultDocument.class)
				.setForm(TEST_FORM)
				.save();
		org.riverframework.lotusnotes.DefaultDocumentCollection rIterator = rDatabase.getAllDocuments();

		rDoc = null;

		while (rIterator.hasNext()) {
			rDoc = rIterator.next();
		}

		assertTrue("There is a problem getting documents from the database.", rDoc == null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		org.riverframework.lotusnotes.DefaultDocumentCollection rIterator = rDatabase.getAllDocuments();
		rIterator.remove();
	}

}
