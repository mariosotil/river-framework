package org.riverframework.wrapper.lotus.domino._local;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.logging.Logger;

import org.riverframework.River;

public class StressTest extends org.riverframework.wrapper.AbstractStressTest {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();

//		new LoggerHelper(log)
//		.setUseParentHandlers(false)
//		.clearHandlers()
//		.addConsoleHandler()
//		.addFileHandler("D:\\stress-test.txt")
//		.setLevel(Level.INFO);
		
		maxDocumentsForStressTest = 100;

		log.setUseParentHandlers(false);
		log.fine("Starting test");
	}
	
	@AfterClass
	public static void after() {		
		log.fine("Test done");

		NotesThread.stermThread();
	}
}
