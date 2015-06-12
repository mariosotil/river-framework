package org.riverframework.wrapper;

public interface View<N> extends Base<N> {
	public Document<N> getDocumentByKey(String key);

	public DocumentIterator<N> getAllDocuments();

	public DocumentIterator<N> getAllDocumentsByKey(Object key);

	public View<N> refresh();

	public DocumentIterator<N> search(String query);

	public void delete();
}
