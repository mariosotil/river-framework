package org.riverframework;

import java.util.Map;

public interface Session {
	public static final String PREFIX = "RIVER_";

	public Session open(String... parameters);

	public <U extends org.riverframework.Database<?>> U getDatabase(Class<U> type, String... parameters);

	public Map<String, View<?>> getViewMap();

	public boolean isOpen();

	public void close();
}
