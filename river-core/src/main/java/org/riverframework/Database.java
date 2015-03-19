package org.riverframework;

public interface Database {
	public boolean isOpen();

	public Database open(String... location);

	public Database getMainReplica();

	public <U extends View> U getView(Class<U> type, String... parameters);

	public <U extends Document> U createDocument(Class<U> clazz, String... parameters);

	public <U extends Document> U getDocument(Class<U> clazz, String... parameters);

	public <U extends Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters);

	public DocumentCollection getAllDocuments();

	public Counter getCounter(String key);

}
