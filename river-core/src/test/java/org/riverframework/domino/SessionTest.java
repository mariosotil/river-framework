package org.riverframework.domino;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SessionTest {
	@Before
	public void open() {

	}

	@Test
	public void testSession() {
		NotesThread.sinitThread();

		Session session = DefaultSession.getInstance().open(Credentials.getPassword());

		assertTrue("Notes Session could not be retrieved", session.isOpen());
		assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
				session.getUserName().equals(""));

		DefaultSession.getInstance().close();

		NotesThread.stermThread();
	}

	@After
	public void close() {
	}
}
