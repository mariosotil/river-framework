package org.riverframework.wrapper.hazelcast;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class SessionTest extends org.riverframework.wrapper.AbstractSessionTest {
	@BeforeClass
	public static void beforeClass() {
		Context.initServer();
	}
	
	@AfterClass
	public static void afterClass() {
		Context.stopServer();
	}
}
