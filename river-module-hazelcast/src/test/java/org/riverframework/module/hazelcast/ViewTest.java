package org.riverframework.module.hazelcast;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ViewTest extends org.riverframework.module.AbstractViewTest {
	@BeforeClass
	public static void beforeClass() {
		Context.initServer();
	}
	
	@AfterClass
	public static void afterClass() {
		Context.stopServer();
	}
}