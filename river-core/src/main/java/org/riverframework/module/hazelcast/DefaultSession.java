package org.riverframework.module.hazelcast;

import org.riverframework.module.Database;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DefaultSession implements org.riverframework.module.Session {

	@Inject
	public DefaultSession(@Assisted MockSession obj) {
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
