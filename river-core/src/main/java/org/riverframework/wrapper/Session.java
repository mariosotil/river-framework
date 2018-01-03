package org.riverframework.wrapper;

public interface Session<N> extends Base<N> {
	Database<?> createDatabase(String... location);

	Database<?> getDatabase(String... parameters);

	String getUserName();

	Factory<?> getFactory();

	@Override
	void close();
}
