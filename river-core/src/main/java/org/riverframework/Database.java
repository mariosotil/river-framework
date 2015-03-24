package org.riverframework;

import org.riverframework.domino.DocumentCollection;
import org.riverframework.domino.Session;

public interface Database {
	public Session getSession();
	
	public boolean isOpen();

	public String getServer();

	public String getFilePath();

	public String getName();

	public Database getMainReplica();

	public View<?> getView(String... parameters);

	public Document createDocument(String... parameters);

	public Document getDocument(String... parameters);

	public DocumentCollection getAllDocuments();

	public DocumentCollection search(String query);

	public Database refreshSearchIndex();
	
	public Counter getCounter(String key);

}
