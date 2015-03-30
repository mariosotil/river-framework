package org.riverframework.core.lotus.domino;

import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;

public class DocumentCollectionTest extends org.riverframework.core.AbstractDocumentCollectionTest {
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
