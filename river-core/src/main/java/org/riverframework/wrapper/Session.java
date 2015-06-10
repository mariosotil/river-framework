package org.riverframework.wrapper;

public interface Session extends Base {
	public org.riverframework.wrapper.Database createDatabase(String... location);

	public org.riverframework.wrapper.Database getDatabase(String... parameters);

	public String getUserName();

	@SuppressWarnings("rawtypes")
	public Factory getFactory();

	@Override
	public void close();
}
