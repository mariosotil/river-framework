package org.riverframework.fw;

import java.util.Map;

public abstract class AbstractSession implements org.riverframework.Session {
	// public static final boolean USE_DOCUMENT_POOL = true;
	// public static final boolean DO_NOT_USE_DOCUMENT_POOL = false;

	public static final String PREFIX = "RIVER_";

	protected static org.riverframework.Session INSTANCE;

	protected AbstractSession() {
		// Exists only to defeat instantiation.
	}

	public static org.riverframework.Session getInstance() {
		return INSTANCE;
	}

	@Override
	public abstract org.riverframework.Session open(String... parameters);

	@Override
	public abstract void close();

	@Override
	public abstract boolean isOpen();

	@Override
	public abstract <U extends org.riverframework.Database<?>> U getDatabase(Class<U> type, String... parameters);

	@Override
	public abstract Map<String, org.riverframework.View<?>> getViewMap();

	@Override
	protected void finalize() throws Throwable {
		close();
	}

}
