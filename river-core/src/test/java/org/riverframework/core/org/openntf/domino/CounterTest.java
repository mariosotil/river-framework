package org.riverframework.core.org.openntf.domino;

import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;

public class CounterTest extends org.riverframework.core.AbstractCounterTest {
	@Before
	public void open() {
		NotesThread.sinitThread();
		super.open();
	}

	@After
	public void close() {
		super.close();
		NotesThread.stermThread();
	}
}
