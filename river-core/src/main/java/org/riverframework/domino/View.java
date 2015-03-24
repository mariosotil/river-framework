package org.riverframework.domino;

public interface View extends org.riverframework.View<org.riverframework.domino.Document> {
	@Override
	public org.riverframework.domino.Database getDatabase();

	@Override
	public org.riverframework.domino.DocumentCollection getAllDocuments();

	@Override
	public org.riverframework.domino.Document getDocumentByKey(String key);

	public <U extends org.riverframework.domino.Document> U getDocumentByKey(Class<U> clazz, String key);

	@Override
	public org.riverframework.domino.DocumentCollection getAllDocumentsByKey(Object key);

	@Override
	public org.riverframework.domino.View refresh();
	
	public org.riverframework.domino.View filter(String query);

	@Override
	public org.riverframework.domino.Document next();

}
