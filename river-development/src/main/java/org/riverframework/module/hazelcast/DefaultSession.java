package org.riverframework.module.hazelcast;

import org.riverframework.module.Database;

public class DefaultSession implements org.riverframework.module.Session {

	public DefaultSession(MockSession obj) {
	}

	@Override
	public Object getReferencedObject() {
		return null;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public Database getDatabase(String... location) {
		return null;
	}

	@Override
	public String getUserName() {
		return "";
	}

	@Override
	public void close() {

	}
}
