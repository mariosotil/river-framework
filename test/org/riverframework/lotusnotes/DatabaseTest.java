package org.riverframework.lotusnotes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.Document;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;

public class DatabaseTest {
	final DefaultSession session = DefaultSession.getInstance();
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	private DefaultDatabase rDatabase = null;
	private VacationDatabase rVacationDatabase = null;

	@Before
	public void init() {
		try {
			NotesThread.sinitThread();
			// session = NotesFactory.createSession(Credentials.getServer(), Credentials.getUser(),
			// Credentials.getPassword());
			session.open(Context.getServer(), Context.getUser(), Context.getPassword());

			rDatabase = session.getDatabase(DefaultDatabase.class, Context.getServer(), Context.getDatabase());
			rVacationDatabase = session.getDatabase(VacationDatabase.class, Context.getServer(), Context.getDatabase());

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
	public void testCreateAndGetDocument() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDocument = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("There is a problem creating a new document in the test database.", rDocument.isOpen());

		String universalId = rDocument
				.setField("TEST_FIELD", "YES")
				.setField("Form", "TestForm")
				.save(false)
				.getUniversalId();
		rDocument = null;

		rDocument = rDatabase.getDocument(DefaultDocument.class, universalId);

		assertTrue("There is a problem getting the document created in the test database.",
				rDocument.isOpen());
	}

	// @Test
	// public void testCreateAndGetGraph() {
	// assertTrue("The test database could not be opened.", rDatabase.isOpen());
	//
	// org.riverframework.development.Relation rRelation = rDatabase.createRelation(TEST_GRAPH);
	//
	// assertTrue("There is a problem creating a new graph in the test database.", rRelation.isOpen());
	//
	// String universalId = rRelation.save(org.riverframework.Document.FORCE_SAVE).getUniversalId();
	// rRelation = null;
	//
	// rRelation = rDatabase.getRelation(universalId);
	//
	// assertTrue("There is a problem getting the graph created in the test database.",
	// rRelation.isOpen());
	// }

	@Test
	public void testGetView() {
		assertTrue("The test database could not be instantiated.", rDatabase != null);
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultView rView = rDatabase.getView(DefaultView.class, TEST_VIEW);

		assertTrue("There is a problem getting the view created in the test database.", rView.isOpen());
	}

	// @Test
	// public void testGetCounter() {
	// assertTrue("The test database could not be opened.", rDatabase.isOpen());
	//
	// final int REPEATS = 10;
	// RandomString rs = new RandomString(10);
	// String key = rs.nextString();
	// int oldCounter = rDatabase.getCounter(key);
	// int newCounter = 0;
	//
	// assertTrue("There is a problem with the first calling to the counter.", oldCounter > 0);
	//
	// for (int i = 0; i < REPEATS; i++) {
	// newCounter = rDatabase.getCounter(key);
	// }
	//
	// assertTrue("There is a problem with the re-calling to the counter.",
	// oldCounter + REPEATS == newCounter);
	// }

	// @Test
	// public void testGetDocumentIndexView() {
	// assertTrue("The test database could not be opened.", rDatabase.isOpen());
	//
	// org.riverframework.View rView = rDatabase.getDocumentIndex();
	//
	// assertTrue("There is a problem opening the Document Index view.", rView.isOpen());
	// }

	// @Test
	// public void testGetRelationIndexView() {
	// assertTrue("The test database could not be opened.", rDatabase.isOpen());
	//
	// org.riverframework.View rView = rDatabase.getRelationIndex();
	//
	// assertTrue("There is a problem opening the Relation Index view.", rView.isOpen());
	// }

	/*
	 * Testing makeDocument
	 */
	static class VacationDatabase extends org.riverframework.lotusnotes.DefaultDatabase {
		public VacationDatabase(org.riverframework.lotusnotes.DefaultSession s) {
			super(s);
		}

		protected VacationDatabase(org.riverframework.lotusnotes.DefaultSession s, lotus.domino.Database obj) {
			super(s, obj);
		}

		public VacationDatabase(org.riverframework.lotusnotes.DefaultSession s, String... location) {
			super(s, location);
		}
	}

	static class VacationRequest extends org.riverframework.lotusnotes.DefaultDocument {

		public VacationRequest(DefaultDatabase d, Document doc) {
			super(d, doc);
		}

	}

	@Test
	public void testCreateAndGetVacationRequest() {
		assertTrue("The test database could not be opened as a VacationDatabase.",
				rVacationDatabase.isOpen());

		VacationRequest rDocument = rVacationDatabase.createDocument(VacationRequest.class);

		assertTrue("There is a problem creating a new VacationRequest in the test database.",
				rDocument.isOpen());

		String universalId = rDocument
				.setField("TEST_FIELD", "YES")
				.setField("Form", "TestForm")
				.save(false)
				.getUniversalId();

		rDocument = null;

		rDocument = rVacationDatabase.getDocument(VacationRequest.class, universalId);

		assertTrue("There is a problem getting a VacationRequest created in the test database.",
				rDocument.isOpen());
	}

	static class NoIndexedRequest extends DefaultDocument {
		protected NoIndexedRequest(DefaultDatabase d, Document doc) {
			super(d, doc);
		}
	}

	static class IndexedRequest extends DefaultDocument {
		protected IndexedRequest(DefaultDatabase d, Document doc) {
			super(d, doc);
		}
	}

	@Test
	public void testGetIndex() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		assertFalse("The SimpleRequest should not be indexed.", rDatabase.getIndex(NoIndexedRequest.class).isOpen());

		assertTrue("The IndexedRequest should be indexed.", rDatabase.getIndex(IndexedRequest.class).isOpen());

	}

}
