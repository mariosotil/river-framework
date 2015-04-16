package org.riverframework.module.hazelcast;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DocumentListTest extends org.riverframework.module.AbstractDocumentListTest {
	@BeforeClass
	public static void beforeClass() {
		Context.initServer();
	}
	
	@AfterClass
	public static void afterClass() {
		Context.stopServer();
	}
}