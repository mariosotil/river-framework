package org.riverframework.wrapper.org.openntf.domino._local;

import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.riverframework.River;

public class StressTest extends org.riverframework.wrapper.org.openntf.domino.AbstractStressTest {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
	}
	
	@AfterClass
	public static void after() {		
		NotesThread.stermThread();
	}
}
