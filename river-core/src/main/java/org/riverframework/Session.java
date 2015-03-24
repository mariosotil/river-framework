package org.riverframework;

public interface Session {
	public static final String OBJECT_PREFIX = "RIVER_";
	public static final String FIELD_PREFIX = "RIVER_";

	public String getUUID();
	
	public Session open(String... parameters);

	public <U extends Database> U getDatabase(Class<U> type, String... parameters);

	public boolean isOpen();

	public String getUserName();

	public void close();
}
