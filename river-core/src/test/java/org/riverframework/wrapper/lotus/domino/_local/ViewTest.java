package org.riverframework.wrapper.lotus.domino._local;

import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;

public class ViewTest extends org.riverframework.wrapper.AbstractViewTest {
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