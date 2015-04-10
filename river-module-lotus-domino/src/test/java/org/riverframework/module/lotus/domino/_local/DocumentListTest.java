package org.riverframework.module.lotus.domino._local;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DocumentListTest extends org.riverframework.module.AbstractDocumentListTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
	}

	@AfterClass
	public static void after() {
		NotesThread.stermThread();
	}

}