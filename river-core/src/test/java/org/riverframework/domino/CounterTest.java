package org.riverframework.domino;

import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CounterTest {
	private Session session = DefaultSession.getInstance();
	private org.riverframework.domino.Database rDatabase = null;

	@Before
	public void init() {
		NotesThread.sinitThread();

		session.open(Credentials.getPassword());
		rDatabase = session.getDatabase(DefaultDatabase.class, "", Context.getDatabase());
		rDatabase.getAllDocuments().removeAll();
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