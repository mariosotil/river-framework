package org.riverframework.wrapper.openntf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.core.Credentials;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Session;

public class SessionTest {
	@Before
	public void open() {
		NotesThread.sinitThread();
	}

	@Test
	public void testSession() {
		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		DefaultSession session = new DefaultSession((String) null, (String) null, password);

		assertTrue("Notes Session could not be retrieved", session.isOpen());
		assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
				session.getUserName().equals(""));
	}

	@Test
	public void testOpeningRemoteDatabase() {
		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		Session session = new DefaultSession((String) null, (String) null, password);

		assertTrue("Notes Session could not be retrieved", session.isOpen());

		Database database = session.getDatabase(Context.getServer(), Context.getRemoteDatabase());

		assertTrue("Remote database could not be opened", database.isOpen());
		assertTrue("Remote database's path is not equal to the requested.", database.getFilePath().equals(Context.getRemoteDatabase()));
		assertTrue("Remote database's server is not equal to the requested.", database.getServer().equals(Context.getServer()));

	}

	@After
	public void close() {
		NotesThread.stermThread();
	}
}
