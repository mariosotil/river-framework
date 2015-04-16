package org.riverframework.module.hazelcast;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DocumentTest extends org.riverframework.module.AbstractDocumentTest {
	@BeforeClass
	public static void beforeClass() {
		Context.initServer();
	}
	
	@AfterClass
	public static void afterClass() {
		Context.stopServer();
	}
}