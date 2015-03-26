package org.riverframework.core;

import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Counter;
import org.riverframework.RandomString;
import org.riverframework.Session;
import org.riverframework.core.DefaultDatabase;
import org.riverframework.core.DefaultSession;

public class CounterTest {
	private Session session = DefaultSession.getInstance();
	private org.riverframework.Database rDatabase = null;
	private Context context = Context.getInstance();

	@Before
	public void init() {
		NotesThread.sinitThread();

		session.open(context.getSession());
		rDatabase = session.getDatabase(DefaultDatabase.class, "", context.getDatabase());
		rDatabase.getAllDocuments().deleteAll();
	}

	@After
	public void close() {
		session.close();
		NotesThread.stermThread();
	}

	@Test
	public void testCounter() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		RandomString rs = new RandomString(10);

		String key = rs.nextString();
		Counter counter = rDatabase.getCounter(key);

		long n = counter.getCount();
		long n1 = counter.getCount();

		assertTrue("The counter does not return two consecutives counters for the key '" + key + "'. It returned n=" + n + " and n1=" + n1,
				n1 == n + 1);
	}
}
