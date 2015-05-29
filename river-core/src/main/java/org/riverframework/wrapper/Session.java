package org.riverframework.wrapper;

public interface Session extends Base {
	public org.riverframework.wrapper.Database getDatabase(String... parameters);

	public String getUserName();

	public Factory getFactory();
	
	@Override
	public void close();
}
