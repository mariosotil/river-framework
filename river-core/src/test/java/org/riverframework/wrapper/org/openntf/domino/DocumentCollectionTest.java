package org.riverframework.wrapper.org.openntf.domino;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DocumentCollectionTest extends org.riverframework.wrapper.AbstractDocumentCollectionTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
	}

	@AfterClass
	public static void after() {
		NotesThread.stermThread();
	}

}