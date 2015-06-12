package org.riverframework.wrapper;

public interface Factory<N> {
	public Session<N> getSession(Object... parameters);

	public Database<N> getDatabase(N obj);

	public Document<N> getDocument(N obj);

	public View<N> getView(N obj);

	public DocumentIterator<N> getDocumentIterator(N obj);

	public void close();

	void logStatus();
}
