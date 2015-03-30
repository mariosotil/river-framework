package org.riverframework.wrapper.lotus.domino._remote;

import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;

public class DocumentCollectionTest extends org.riverframework.wrapper.AbstractDocumentCollectionTest {
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