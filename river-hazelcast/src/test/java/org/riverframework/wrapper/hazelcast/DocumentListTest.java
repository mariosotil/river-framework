package org.riverframework.wrapper.hazelcast;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DocumentListTest extends org.riverframework.wrapper.AbstractDocumentListTest {
	@BeforeClass
	public static void beforeClass() {
		Context.initServer();
	}
	
	@AfterClass
	public static void afterClass() {
		Context.stopServer();
	}
}