package org.riverframework.core.lotus.domino._inject_session;

import lotus.domino.NotesThread;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ViewTest extends org.riverframework.core.AbstractViewTest {
	@BeforeClass
	public static void before() {
		NotesThread.sinitThread();
	}

	@AfterClass
	public static void after() {
		NotesThread.stermThread();
	}

}
