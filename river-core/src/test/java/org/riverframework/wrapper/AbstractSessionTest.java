package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.core.Credentials;
import org.riverframework.wrapper.Session;

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

	@Test
	public void testSession() {
		String password = Credentials.getPassword();
		assertFalse("Password can't be an empty string", password.equals(""));

		for (int i = 0; i < 5; i++) {
			Session session = (Session) context.getSession().getWrapperObject();

			assertTrue("The Session could not be retrieved at the interation " + i, session.isOpen());
			assertFalse("There's a problem with the Session at the iteration " + i + ". I can't retrieve the current user name.",
					session.getUserName().equals(""));

			context.closeSession();
		}
	}
}
