package org.riverframework.wrapper.lotus.domino._remote;

import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;

public class DocumentTest  extends org.riverframework.wrapper.AbstractDocumentTest {
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