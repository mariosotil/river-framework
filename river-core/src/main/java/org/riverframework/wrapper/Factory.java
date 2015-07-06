package org.riverframework.wrapper;

public interface Factory<N> {
	public Session<? extends N> getSession(Object... parameters);

	public <U extends N> Database<? extends N> getDatabase(U obj);

	public Document<? extends N> getDocument(String objectId);

	public <U extends N> Document<? extends N> getDocument(U obj);

	public <U extends N> View<?> getView(U obj);

	public <U extends N> DocumentIterator<? extends N, ? extends N> getDocumentIterator(U obj);

	public void close();

	public void cleanUp(Base<? extends N>... except);

	public void logStatus();
}
