package org.riverframework.core.org.openntf.domino._local;

//import java.util.logging.Level;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;
//import org.riverframework.River;

public class DatabaseTest extends org.riverframework.core.AbstractDatabaseTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
				
//		River.setLevel(River.LOG_CORE, Level.FINE);
//		River.setLevel(River.LOG_WRAPPER, Level.SEVERE);
		
//		River.LOG_CORE.fine("Starting test");
	}

	@AfterClass
	public static void after() {
//		River.LOG_CORE.fine("Done");

		NotesThread.stermThread();
	}

}
