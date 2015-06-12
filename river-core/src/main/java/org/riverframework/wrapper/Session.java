package org.riverframework.wrapper;

public interface Session<N> extends Base<N> {
	public Database<N> createDatabase(String... location);

	public Database<N> getDatabase(String... parameters);

	public String getUserName();

	@SuppressWarnings("rawtypes")
	public Factory getFactory();

	@Override
	public void close();
}
