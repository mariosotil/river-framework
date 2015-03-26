package org.riverframework.wrapper;

public interface View {
	public Document getDocumentByKey(String key);

	public boolean isOpen();

	public DocumentCollection getAllDocuments();

	public DocumentCollection getAllDocumentsByKey(Object key);

	public View refresh();

	public DocumentCollection search(String query);
}
