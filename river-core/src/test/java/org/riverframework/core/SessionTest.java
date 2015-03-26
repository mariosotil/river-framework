package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Database;
import org.riverframework.Session;
import org.riverframework.core.Credentials;
import org.riverframework.core.DefaultDatabase;
import org.riverframework.core.DefaultSession;

public class SessionTest {
	private Context context = Context.getInstance();

	@Before
	public void open() {
		NotesThread.sinitThread();
	}

	@Test
	public void testSession() {
		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		Session session = DefaultSession.getInstance().open(context.getSession());

		assertTrue("Notes Session could not be retrieved", session.isOpen());
		assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
				session.getUserName().equals(""));

		DefaultSession.getInstance().close();
	}

	@Test
	public void testSessionUUID() {
		Session session = DefaultSession.getInstance().open(context.getSession());

		assertTrue("Notes Session could not be retrieved", session.isOpen());

		String uuid1 = session.getUUID();

		Database rDatabase = session.getDatabase(DefaultDatabase.class, "", context.getDatabase());

		String uuid2 = rDatabase.getSession().getUUID();

		assertTrue("It was retrieved differents UUID from the first session and the session object from the database", uuid1.equals(uuid2));

		DefaultSession.getInstance().close();
	}

	@Test
	public void testOpeningRemoteDatabase() {
		Session session = DefaultSession.getInstance().open(context.getSession());

		assertTrue("Notes Session could not be retrieved", session.isOpen());

		Database rDatabase = session.getDatabase(DefaultDatabase.class, context.getServer(), context.getRemoteDatabase());

		assertTrue("Remote database could not be opened", rDatabase.isOpen());
		assertTrue("Remote database's path is not equal to the requested.", rDatabase.getFilePath().equals(context.getRemoteDatabase()));
		assertTrue("Remote database's server is not equal to the requested.", rDatabase.getServer().equals(context.getServer()));

		DefaultSession.getInstance().close();
	}

	// 2015.03.25 - This feature does not work in the version 4 of Openntf Domino API
	// @Test
	// public void testOpeningByReplicaID() {
	// Session session = DefaultSession.getInstance().open(Credentials.getPassword());
	//
	// assertTrue("Notes Session could not be retrieved", session.isOpen());
	//
	// Database rDatabase = session.getDatabase(DefaultDatabase.class, context.getServer(), "04257CAC0065BE0C");
	//
	// assertTrue("Remote database could not be opened", rDatabase.isOpen());
	// assertTrue("Remote database's path is not equal to the requested.",
	// rDatabase.getFilePath().equals(context.getRemoteDatabase()));
	// assertTrue("Remote database's server is not equal to the requested.",
	// rDatabase.getServer().equals(context.getServer()));
	//
	// DefaultSession.getInstance().close();
	// }

	@After
	public void close() {
		NotesThread.stermThread();
	}
}
