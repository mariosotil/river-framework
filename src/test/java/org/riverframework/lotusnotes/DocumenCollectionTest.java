package org.riverframework.lotusnotes;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;
import org.riverframework.lotusnotes.base.DefaultDatabase;
import org.riverframework.lotusnotes.base.DefaultDocument;
import org.riverframework.lotusnotes.base.DefaultDocumentCollection;
import org.riverframework.lotusnotes.base.DefaultSession;

public class DocumenCollectionTest {
	final Session session = DefaultSession.getInstance();
	private Database rDatabase = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void init() {
		try {
			session.open(Context.getServer(), Context.getUser(), Context.getPassword());
			rDatabase = session.getDatabase(DefaultDatabase.class, Context.getServer(), Context.getDatabase());
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@After
	public void close() {
		session.close();
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

		org.riverframework.lotusnotes.base.DefaultDocumentCollection rIterator = (DefaultDocumentCollection) rDatabase.getAllDocuments();
		rIterator.remove();
	}

}
