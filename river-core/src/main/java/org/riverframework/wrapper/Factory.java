package org.riverframework.wrapper;

public interface Factory<T> {
	public Database getDatabase(T obj);

	public Document getDocument(T obj);

	public View getView(T obj);

	public DocumentIterator getDocumentIterator(T obj);

	public void close();
}
