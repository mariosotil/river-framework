package org.riverframework.lotusnotes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;

public class SessionTest extends NotesThread {
	@Before
	public void open() {
		try {
			NotesThread.sinitThread();
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Test
	public void testAnonymousSession() {
		try {
			DefaultSession session = (DefaultSession) DefaultSession
					.getInstance()
					.open();

			assertTrue("Notes Session could not be retrieved", session.isOpen());
			assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
					session.getNotesSession().getCommonUserName().equals(""));
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Test
	public void testLoggedSession() {
		try {
			DefaultSession session = (DefaultSession) DefaultSession
					.getInstance()
					.open(Context.getServer(), Context.getUser(), Context.getPassword());

			assertTrue("Notes Session could not be retrieved", session.isOpen());
			assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
					session.getNotesSession().getCommonUserName().equals(""));
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@After
	public void close() {
		DefaultSession.getInstance().close();
		NotesThread.stermThread();
	}
}
