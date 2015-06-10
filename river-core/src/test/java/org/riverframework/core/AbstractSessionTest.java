package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;

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

		for (int i = 0; i < 10; i++) {
			Session session = context.getSession();

			assertTrue("Notes Session could not be retrieved at the iteration " + i, session.isOpen());
			assertFalse("There's a problem with the Session at the iteration " + i + ". I can't retrieve the current user name.",
					session.getUserName().equals(""));

			context.closeSession();
		}
	}

	@Test
	public void testSessionObjectId() {
		Session session = context.getSession();

		assertTrue("Notes Session could not be retrieved", session.isOpen());

		String uuid1 = session.getObjectId();

		Database database = session.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());

		String uuid2 = database.getSession().getObjectId();

		assertTrue("It was retrieved differents UUID from the first session and the session object from the database", uuid1.equals(uuid2));

		context.closeSession();
	}
	
	@Test
	public void testCreateAndRemoveDatabase() {
		Session session = context.getSession();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String name = "DB_" + sdf.format(new Date()) + ".nsf";
		Database db = session.createDatabase(context.getTestDatabaseServer(), name);
		assertTrue("There was a problem creating a new database.", db != null && db.isOpen());
		
		db.delete();
		
		assertFalse("There was a problem deleting the new database.", db.isOpen());
		
		db = session.getDatabase(context.getTestDatabaseServer(), name);
		
		assertFalse("The last database created was not deleted.", db.isOpen());
		
		context.closeSession();
	}

	@After
	public void close() {
	}
}
