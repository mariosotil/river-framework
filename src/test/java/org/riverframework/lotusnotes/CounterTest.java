package org.riverframework.lotusnotes;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;
import org.riverframework.lotusnotes.base.DefaultDatabase;
import org.riverframework.lotusnotes.base.DefaultSession;

public class CounterTest {
	final Session session = DefaultSession.getInstance();
	private org.riverframework.lotusnotes.Database rDatabase = null;

	@Before
	public void init() {
		try {
			session.open(Context.getServer(), Context.getUser(), Context.getPassword());
			rDatabase = session.getDatabase(DefaultDatabase.class, Context.getServer(), Context.getDatabase());

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@After
	public void close() {
		session.close();
	}

	@Test
	public void testCounter() throws InterruptedException {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		RandomString rs = new RandomString(10);

		String key = rs.nextString();
		Counter counter = rDatabase.getCounter();

		long n = counter.generateId(key);
		long n1 = counter.generateId(key);

		assertTrue("The counter does not return two consecutives counters for the key '" + key + "'. It returned n=" + n + " and n1=" + n1,
				n1 == n + 1);
	}
}
