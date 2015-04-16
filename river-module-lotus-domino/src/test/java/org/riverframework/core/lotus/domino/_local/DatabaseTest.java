package org.riverframework.core.lotus.domino._local;

import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.riverframework.River;

public class DatabaseTest extends org.riverframework.core.AbstractDatabaseTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
				
		River.setLevel(River.LOG_CORE, Level.FINE);
		River.setLevel(River.LOG_MODULE, Level.SEVERE);
		
		River.LOG_CORE.fine("Starting test");
	}

	@AfterClass
	public static void after() {
		River.LOG_CORE.fine("Done");

		NotesThread.stermThread();
	}

}
