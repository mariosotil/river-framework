package org.riverframework.wrapper;

public interface Factory<N> {
	public Session<?> getSession(Object... parameters);

	public Database<?> getDatabase(N obj);

	public Document<?> getDocument(String objectId);

	public Document<?> getDocument(N obj);

	public View<?> getView(N obj);

	public DocumentIterator<?, ?> getDocumentIterator(N obj);

	public void close();

	public void cleanUp(Base<N>... except);

	public void logStatus();
}
