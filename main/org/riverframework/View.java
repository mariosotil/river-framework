package org.riverframework;

public interface View<T> extends java.util.Iterator<org.riverframework.Document<?>> {
	public boolean isOpen();

	public org.riverframework.Document<?> getDocumentByKey(Object key);

	public org.riverframework.DocumentCollection<?> getAllDocumentsByKey(Object key);

	public org.riverframework.View<T> refresh();
}
