package org.riverframework.module;

public interface Session extends Base {
	public boolean isOpen();

	public org.riverframework.module.Database getDatabase(String... location);

	public String getUserName();

	public void close();
}
