package org.riverframework.wrapper;


public interface Factory {
	public Database getDatabase(Object obj);

	public Document getDocument(Object obj);

	public View getView(Object obj);

	public DocumentIterator getDocumentIterator(Object obj);
}
