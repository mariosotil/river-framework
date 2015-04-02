package org.riverframework.module;

public interface Session extends Base {
	public boolean isOpen();

	public org.riverframework.module.Database getDatabase(String... parameters);

	public String getUserName();

	@Override
	public void close();
}
