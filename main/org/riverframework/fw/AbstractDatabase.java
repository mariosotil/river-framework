package org.riverframework.fw;

/*
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public abstract class AbstractDatabase<T> implements org.riverframework.Database<T> {
	protected T database = null;
	protected org.riverframework.Session session = null;

	public AbstractDatabase(org.riverframework.Session s) {
		session = s;
	}

	protected AbstractDatabase(org.riverframework.Session s, T obj) {
		session = s;
		open(obj);
	}

	public AbstractDatabase(org.riverframework.Session s, String... location) {
		session = s;
		open(location);
	}

	@Override
	public org.riverframework.Database<T> open(T obj) {
		database = obj;
		return this;
	}

	@Override
	public abstract org.riverframework.Database<T> open(String... location);

	@Override
	public boolean isOpen() {
		return (database != null);
	}

	@Override
	public abstract <U extends org.riverframework.Document<?>> U getDocument(Class<U> type, String... parameters);

	@Override
	public abstract <U extends org.riverframework.Document<?>> U createDocument(Class<U> type, String... parameters);

	@Override
	public abstract <U extends org.riverframework.View<?>> U getView(Class<U> type, String... parameters);

	@Override
	public abstract org.riverframework.DocumentCollection<?> getAllDocuments();

	@Override
	public abstract org.riverframework.View<?> getIndex(Class<?> clazz);

	protected void close() {
		if (isOpen()) {
			database = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}
}
