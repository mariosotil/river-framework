package org.riverframework.core.org.openntf.domino._local;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.Test;
import org.riverframework.River;
import org.riverframework.Session;
import org.riverframework.core.Credentials;

public class LotusNotesSessionTest {

	@Test
	public void testSessionClosingFromRiverFactory() {
		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		for (int i = 0; i < 10; i++) {
			NotesThread.sinitThread();
			
			Session session = River.getSession(River.LOTUS_DOMINO,
					(String) null, (String) null, Credentials.getPassword());

			assertTrue("Notes Session could not be retrieved at the iteration " + i, session.isOpen());
			assertFalse("There's a problem with the Session at the iteration " + i + ". I can't retrieve the current user name.",
					session.getUserName().equals(""));

			River.closeSession(River.LOTUS_DOMINO);
			
			NotesThread.stermThread();
		}
	}

	@Test
	public void testSessionClosingIt() {
		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		for (int i = 0; i < 10; i++) {
			NotesThread.sinitThread();

			Session session = River.getSession(River.LOTUS_DOMINO,
					(String) null, (String) null, Credentials.getPassword());

			assertTrue("Notes Session could not be retrieved at the iteration " + i, session.isOpen());
			assertFalse("There's a problem with the Session at the iteration " + i + ". I can't retrieve the current user name.",
					session.getUserName().equals(""));

			session.close();
			
			NotesThread.stermThread();
		}
	}
}
