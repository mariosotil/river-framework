package org.riverframework;

@SuppressWarnings("rawtypes")
public interface View<T> extends java.util.Iterator<T> {
	public boolean isOpen();

	public <U extends Document> U getDocumentByKey(Class<U> clazz, Object key);

	public DocumentCollection getAllDocuments();

	public DocumentCollection getAllDocumentsByKey(Object key);

	public View refresh();
}
