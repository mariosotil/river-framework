package org.riverframework.wrapper.hazelcast;

import org.riverframework.wrapper.Database;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DefaultSession implements org.riverframework.wrapper.Session {

	@Inject
	public DefaultSession(@Assisted String... parameters) {
	}

	@Override
	public Object getWrappedObject() {
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
