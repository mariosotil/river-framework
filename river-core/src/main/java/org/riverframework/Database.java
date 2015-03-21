package org.riverframework;

import org.riverframework.domino.DocumentCollection;

public interface Database {
	public boolean isOpen();

	public String getServer();

	public String getFilePath();

	public String getName();

	public Database getMainReplica();

	public <U extends View> U getView(Class<U> type, String... parameters);

	public Document createDocument(String... parameters);

	public Document getDocument(String... parameters);

	public DocumentCollection getAllDocuments();

	public DocumentCollection search(String query);

	public Counter getCounter(String key);

}
