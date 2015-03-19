package org.riverframework.lotusnotes;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ContextTest {

	@Test
	public void test() {
		String server = Context.getServer();
		assertTrue("The server can't be found.", server != "");
		String user = Context.getUser();
		assertTrue("The user can't be found.", user != "");
		String password = Context.getPassword();
		assertTrue("The password can't be found.", password != "");
		String database = Context.getDatabase();
		assertTrue("The database can't be found.", database != "");
	}
}
