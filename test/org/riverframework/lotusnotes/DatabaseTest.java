package org.riverframework.lotusnotes;

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
			session.open(Credentials.getServer(), Credentials.getUser(), Credentials.getPassword());

			rDatabase = session.getDatabase(DefaultDatabase.class, "server1/wtres", "trabajo\\FW\\beach.nsf");
			rVacationDatabase = session.getDatabase(VacationDatabase.class, "server1/wtres", "trabajo\\FW\\Beach.nsf");

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
		public VacationDatabase() {
			super();
		}

		public VacationDatabase(lotus.domino.Database obj) {
			super(obj);
		}

		public VacationDatabase(String... location) {
			super(location);
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

	// @Test
	// public void testDocumentFactoryStrict() {
	// assertTrue("The test database could not be opened as a VacationDatabase.",
	// rVacationDatabase.isOpen());
	// assertTrue(
	// "Expected that the test database as a VacationDatabase have strictGetDocument = true.",
	// rVacationDatabase.isDocumentFactoryStrict());
	//
	// VacationRequest rRequest = (VacationRequest) rVacationDatabase.createDocument("VacationRequest");
	//
	// assertTrue("There is a problem creating a valid VacationRequest object in the test database.",
	// rRequest.isOpen());
	//
	// org.riverframework.Document rDocument = rVacationDatabase
	// .createDocument("ClassNoDefinedAtMakeDocument");
	//
	// assertTrue(
	// "In a database with strictGetDocument = true, it was getting an object from a invalid class in the test database.",
	// rDocument == null);
	//
	// rDocument = null;
	// rDocument = rDatabase.createDocument(TEST_FORM).setForm(TEST_FORM)
	// .setField("SOME_FIELD", "SOME_DATA").save();
	// String universalId = rDocument.getUniversalId();
	// assertTrue("There is a problem creating a new document as common Document using rDatabase.",
	// rDocument.isOpen());
	//
	// rDocument = null;
	// rDocument = rDatabase.getDocument(universalId);
	// assertTrue("There is a problem getting the document created as a common Document.",
	// rDocument.isOpen());
	//
	// rDocument = null;
	// rDocument = rVacationDatabase.getDocument(universalId);
	//
	// assertTrue(
	// "A document with no class registered at rVacationDatabase was getting as a common Document.",
	// rDocument == null);
	//
	// }

}
