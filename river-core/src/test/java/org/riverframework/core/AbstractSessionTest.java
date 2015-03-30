package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.Database;
import org.riverframework.Session;

public abstract class AbstractSessionTest {
	protected Context context = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				Class<?> clazz = Class.forName(this.getClass().getPackage().getName() + ".Context");
				if (org.riverframework.Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testSession() {
		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		Session session = DefaultSession.getInstance().setWrappedSession(context.getSession());

		assertTrue("Notes Session could not be retrieved", session.isOpen());
		assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
				session.getUserName().equals(""));

		DefaultSession.getInstance().close();
	}

	@Test
	public void testSessionUUID() {
		Session session = DefaultSession.getInstance().setWrappedSession(context.getSession());

		assertTrue("Notes Session could not be retrieved", session.isOpen());

		String uuid1 = session.getUUID();

		Database database = session.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());

		String uuid2 = database.getSession().getUUID();

		assertTrue("It was retrieved differents UUID from the first session and the session object from the database", uuid1.equals(uuid2));

		DefaultSession.getInstance().close();
	}

	@Test
	public void testOpeningRemoteDatabase() {
		Session session = DefaultSession.getInstance().setWrappedSession(context.getSession());

		assertTrue("Notes Session could not be retrieved", session.isOpen());

		Database database = session.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());

		assertTrue("Remote database could not be opened", database.isOpen());

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
	}
}
