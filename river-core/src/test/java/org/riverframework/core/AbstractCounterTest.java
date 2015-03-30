package org.riverframework.core;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.Counter;
import org.riverframework.Database;
import org.riverframework.RandomString;
import org.riverframework.Session;

public abstract class AbstractCounterTest {
	protected Session session = null;
	protected Database database = null;
	protected Context context = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				String className = this.getClass().getPackage().getName() + ".Context";
				Class<?> clazz = Class.forName(className);
				if (org.riverframework.Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = context.getSession();
				database = session.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());
				database.getAllDocuments().deleteAll();
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
	public void testCounter() {
		assertTrue("The test database could not be opened.", database.isOpen());

		RandomString rs = new RandomString(10);

		String key = rs.nextString();
		Counter counter = database.getCounter(key);

		long n = counter.getCount();
		long n1 = counter.getCount();

		assertTrue("The counter does not return two consecutives counters for the key '" + key + "'. It returned n=" + n + " and n1=" + n1,
				n1 == n + 1);
	}
}
