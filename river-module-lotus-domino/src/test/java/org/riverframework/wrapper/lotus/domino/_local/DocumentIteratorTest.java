package org.riverframework.wrapper.lotus.domino._local;

//import java.util.logging.Level;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;
//import org.riverframework.River;

public class DocumentIteratorTest extends org.riverframework.wrapper.AbstractDocumentIteratorTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
		
//		River.setLevel(River.LOG_WRAPPER_LOTUS_DOMINO, Level.FINEST);
//		
//		River.LOG_WRAPPER_LOTUS_DOMINO.fine("Starting test");
	}

	@AfterClass
	public static void after() {
//		River.LOG_WRAPPER_LOTUS_DOMINO.fine("Done");
		
		NotesThread.stermThread();
	}

}