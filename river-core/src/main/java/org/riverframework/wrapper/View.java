package org.riverframework.wrapper;

public interface View extends Base {
	public Document getDocumentByKey(String key);

	public DocumentList getAllDocuments();

	public DocumentList getAllDocumentsByKey(Object key);

	public View refresh();

	public DocumentList search(String query);
}
