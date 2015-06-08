package org.riverframework.wrapper;

public interface View extends Base {
	public Document getDocumentByKey(String key);

	public DocumentIterator getAllDocuments();

	public DocumentIterator getAllDocumentsByKey(Object key);

	public View refresh();

	public DocumentList search(String query);

	public void delete();
}
