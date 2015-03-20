package org.riverframework.domino;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;
import org.riverframework.domino.DefaultSession;
import org.riverframework.domino.Session;

public class SessionTest {
	@Before
	public void open() {

	}

	@Test
	public void testSession() {
		NotesThread.sinitThread();

		try {
			Session session = DefaultSession.getInstance().open("passw0rd");

			assertTrue("Notes Session could not be retrieved", session.isOpen());
			assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
					session.getNotesSession().getCommonUserName().equals(""));

			DefaultSession.getInstance().close();
		} catch (Exception e) {
			throw new RiverException(e);
		}

		NotesThread.stermThread();
	}

	@After
	public void close() {
	}
}
