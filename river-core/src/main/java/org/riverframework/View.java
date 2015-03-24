package org.riverframework;

public interface View<T> extends java.util.Iterator<T> {
	public Database getDatabase();

	public boolean isOpen();

	public Document getDocumentByKey(String key);

	public DocumentCollection<?> getAllDocuments();

	public DocumentCollection<?> getAllDocumentsByKey(Object key);

	public View<?> refresh();
}
