package org.riverframework.wrapper;

public interface Session {
	public boolean isOpen();

	public org.riverframework.wrapper.Database getDatabase(String... location);

	public String getUserName();

	public void close();
}
