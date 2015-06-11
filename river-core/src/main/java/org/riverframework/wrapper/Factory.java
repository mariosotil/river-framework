package org.riverframework.wrapper;

public interface Factory<N> {
	public Database getDatabase(N obj);

	public Document getDocument(N obj);

	public View getView(N obj);

	public DocumentIterator getDocumentIterator(N obj);

	public void close();

	void logStatus();
}
