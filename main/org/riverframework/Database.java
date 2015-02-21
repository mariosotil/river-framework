package org.riverframework;

public interface Database<T> {
	public boolean isOpen();

	public org.riverframework.Database<T> open(T obj);

	public org.riverframework.Database<T> open(String... location);

	public <U extends org.riverframework.View<?>> U getView(Class<U> type, String... parameters);

	public <U extends org.riverframework.Document<?>> U createDocument(Class<U> type, String... parameters);

	public <U extends org.riverframework.Document<?>> U getDocument(Class<U> type, String... parameters);

	public org.riverframework.DocumentCollection<?> getAllDocuments();
}
