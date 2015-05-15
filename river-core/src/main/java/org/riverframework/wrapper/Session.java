package org.riverframework.wrapper;

public interface Session extends Base {
	public org.riverframework.wrapper.Database getDatabase(String... parameters);

	public String getUserName();

	@Override
	public void close();
}
