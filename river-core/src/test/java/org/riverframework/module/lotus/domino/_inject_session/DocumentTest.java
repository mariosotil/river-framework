package org.riverframework.module.lotus.domino._inject_session;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DocumentTest extends org.riverframework.module.AbstractDocumentTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
	}

	@AfterClass
	public static void after() {
		NotesThread.stermThread();
	}

}