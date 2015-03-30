package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.core.Credentials;

public abstract class AbstractSessionTest {
	protected Context context = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				Class<?> clazz = Class.forName(this.getClass().getPackage().getName() + ".Context");
				if (Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void close() {
		context.closeSession();
	}

	@Test
	public void testSession() {
		String password = Credentials.getPassword();
		assertFalse("Password can't be an empty string", password.equals(""));

		Session session = (Session) context.getSession().getWrappedObject();

		assertTrue("Notes Session could not be retrieved", session.isOpen());
		assertFalse("There's a problem with the Session. I can't retrieve the current user name.",
				session.getUserName().equals(""));
	}

	@Test
	public void testOpeningRemoteDatabase() {
		String password = Credentials.getPassword();
		assertFalse("Password can be an empty string", password.equals(""));

		Session session = (Session) context.getSession().getWrappedObject();

		assertTrue("Notes Session could not be retrieved", session.isOpen());

		Database database = session.getDatabase(context.getRemoteDatabaseServer(), context.getRemoteDatabasePath());

		assertTrue("Remote database could not be opened", database.isOpen());
	}
}
