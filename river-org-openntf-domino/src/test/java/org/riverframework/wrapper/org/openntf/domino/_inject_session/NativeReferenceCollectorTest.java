package org.riverframework.wrapper.org.openntf.domino._inject_session;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;

public class NativeReferenceCollectorTest extends org.riverframework.wrapper.org.openntf.domino.AbstractStressTest {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();

		River.setLevel(log, Level.FINEST);
		log.setUseParentHandlers(false);
		log.fine("Starting test");
	}

	@AfterClass
	public static void after() {		
		log.fine("Test done");

		NotesThread.stermThread();
	}
}
