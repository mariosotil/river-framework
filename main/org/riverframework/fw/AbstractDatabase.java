package org.riverframework.fw;

/*
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public abstract class AbstractDatabase<T> implements org.riverframework.Database<T> {
	protected T database = null;

	public AbstractDatabase() {

	}

	public AbstractDatabase(T obj) {
		open(obj);
	}

	public AbstractDatabase(String... location) {
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
