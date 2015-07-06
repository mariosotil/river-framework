package org.riverframework.wrapper;

public interface Session<N> extends Base<N> {
	public Database<?> createDatabase(String... location);

	public Database<?> getDatabase(String... parameters);

	public String getUserName();

	public Factory<?> getFactory();

	@Override
	public void close();
}
