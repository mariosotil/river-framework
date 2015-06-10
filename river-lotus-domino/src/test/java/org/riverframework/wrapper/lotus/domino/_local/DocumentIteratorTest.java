package org.riverframework.wrapper.lotus.domino._local;


import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DocumentIteratorTest extends org.riverframework.wrapper.AbstractDocumentIteratorTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
	}

	@AfterClass
	public static void after() {
		NotesThread.stermThread();
	}

}