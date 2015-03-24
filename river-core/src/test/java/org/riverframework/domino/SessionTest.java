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
		NotesThread.sinitThread();
	}

	@Test
	public void testSession() {
		Session session = DefaultSession.getInstance().open(Credentials.getPassword());

		assertTrue("Notes Session could not be retrieved", session.isOpen());
		assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
				session.getUserName().equals(""));

		DefaultSession.getInstance().close();
	}

	@Test
	public void testSessionUUID() {
		Session session = DefaultSession.getInstance().open(Credentials.getPassword());

		assertTrue("Notes Session could not be retrieved", session.isOpen());
		
		String uuid1 = session.getUUID();
		
		Database rDatabase = session.getDatabase(DefaultDatabase.class, "", Context.getDatabase());
		
		String uuid2 = rDatabase.getSession().getUUID();
		
		assertTrue("It was retrieved differents UUID from the first session and the session object from the database", uuid1.equals(uuid2));

		DefaultSession.getInstance().close();
	}
	
	@After
	public void close() {
		NotesThread.stermThread();
	}
}
