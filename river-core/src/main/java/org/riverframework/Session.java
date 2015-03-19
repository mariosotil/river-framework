package org.riverframework;


public interface Session {
	public static final String PREFIX = "RIVER_";

	public Session open(String... parameters);

	public <U extends Database> U getDatabase(Class<U> type, String... parameters);

	public boolean isOpen();

	public void close();
}
