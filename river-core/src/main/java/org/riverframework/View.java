package org.riverframework;

public interface View extends Base {
	public Database getDatabase();

	public boolean isOpen();

	public Document getDocumentByKey(String key);

	public <U extends Document> U getDocumentByKey(Class<U> clazz, String key);

	public DocumentCollection getAllDocuments();

	public DocumentCollection getAllDocumentsByKey(Object key);

	public View refresh();

	public DocumentCollection search(String query);
}
