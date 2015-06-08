package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	@Test
	public void testSession() {
		String password = Credentials.getPassword();
		assertFalse("Password can't be an empty string", password.equals(""));

		for (int i = 0; i < 5; i++) {
			Session _session = (Session) context.getSession().getWrapperObject();

			assertTrue("The Session could not be opened at the interation " + i, _session.isOpen());
			assertFalse("There's a problem with the Session at the iteration " + i + ". I can't retrieve the current user name.", _session
					.getUserName().equals(""));

			context.closeSession();
		}
	}

	@Test
	public void testCreateAndDeleteDatabase() {
		Session _session = (Session) context.getSession().getWrapperObject();
		assertTrue("The Session could not be opened.", _session.isOpen());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Database _db = _session.createDatabase(context.getTestDatabaseServer(), "test_river_" + sdf.format(new Date()) + ".nsf");
		assertTrue("The Database could not be created.", _db.isOpen());

		_db.delete();
		assertFalse("The Database could not be removed.", _db.isOpen());

		context.closeSession();
	}
}
