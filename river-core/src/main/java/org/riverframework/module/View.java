package org.riverframework.module;

public interface View extends Base {
	public Document getDocumentByKey(String key);

	public boolean isOpen();

	public DocumentCollection getAllDocuments();

	public DocumentCollection getAllDocumentsByKey(Object key);

	public View refresh();

	public DocumentCollection search(String query);
}
