package org.riverframework.domino;

public interface Database extends org.riverframework.Database {
	@Override
	public org.riverframework.domino.Session getSession();
	
	@Override
	public org.riverframework.domino.Document createDocument(String... parameters);

	@Override
	public org.riverframework.domino.View getView(String... parameters);

	public <U extends org.riverframework.domino.View> U getView(Class<U> clazz, String... parameters);

	public <U extends org.riverframework.domino.Document> U createDocument(Class<U> clazz, String... parameters);

	@Override
	public org.riverframework.domino.Document getDocument(String... parameters);

	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, String... parameters);

	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters);

	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, lotus.domino.Document doc);

	public org.riverframework.domino.Document getDocument(lotus.domino.Document doc);

	@Override
	public org.riverframework.domino.Database getMainReplica();

	@Override
	public org.riverframework.domino.DocumentCollection getAllDocuments();

	@Override
	public org.riverframework.domino.DocumentCollection search(String query);

	@Override
	public org.riverframework.domino.Database refreshSearchIndex();
		
	@Override
	public org.riverframework.domino.Counter getCounter(String key);
}
