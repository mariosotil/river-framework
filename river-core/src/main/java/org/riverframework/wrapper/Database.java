package org.riverframework.wrapper;

public interface Database {
	public String getObjectId();

	public String getServer();

	public String getFilePath();

	public String getName();

	public boolean isOpen();

	public Document createDocument(String... parameters);

	public Document getDocument(String... parameters);

	public View getView(String... parameters);

	public DocumentCollection getAllDocuments();

	public DocumentCollection search(String query);

	public Database refreshSearchIndex();
}
