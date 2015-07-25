package org.riverframework.wrapper;

public interface View<N> extends Base<N> {
	public Document<?> getDocumentByKey(String key);

	public DocumentIterator<?, ?> getAllDocuments();

	public DocumentIterator<?, ?> getAllDocumentsByKey(Object key);

	public View<N> refresh();

	public DocumentIterator<?, ?> search(String query);

	public View<N> addColumn(String name, String value, boolean sorted);

	public void delete();
}
