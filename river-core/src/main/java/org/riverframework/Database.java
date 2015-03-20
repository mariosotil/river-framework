package org.riverframework;

public interface Database {
	public Database open(String... location);

	public boolean isOpen();

	public String getServer();

	public String getFilePath();

	public String getName();

	public Database getMainReplica();

	public <U extends View> U getView(Class<U> type, String... parameters);

	public <U extends Document> U createDocument(Class<U> clazz, String... parameters);

	public <U extends Document> U getDocument(Class<U> clazz, String... parameters);

	public <U extends Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters);

	public DocumentCollection getAllDocuments();

	public Counter getCounter(String key);

}
