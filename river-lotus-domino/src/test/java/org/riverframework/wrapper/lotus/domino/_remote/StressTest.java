package org.riverframework.wrapper.lotus.domino._remote;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.utils.LoggerHelper;

public class StressTest extends org.riverframework.wrapper.AbstractStressTest {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();

		LoggerHelper lh = new LoggerHelper(log);
		lh.clearHandlers().addConsoleHandler().setLevel(Level.OFF);

		log.setUseParentHandlers(false);
		log.fine("Starting test");
	}

	@AfterClass
	public static void after() {		
		log.fine("Test done");

		NotesThread.stermThread();
	}
}
