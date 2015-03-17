package org.riverframework;

@SuppressWarnings("rawtypes")
public interface View extends java.util.Iterator {
	public boolean isOpen();

	public <U extends Document> U getDocumentByKey(Class<U> clazz, Object key);

	public DocumentCollection getAllDocumentsByKey(Object key);

	public View refresh();
}
