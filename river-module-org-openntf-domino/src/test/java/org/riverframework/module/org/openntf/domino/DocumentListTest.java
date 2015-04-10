package org.riverframework.module.org.openntf.domino;

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