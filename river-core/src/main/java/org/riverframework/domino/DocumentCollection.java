package org.riverframework.domino;


public interface DocumentCollection extends org.riverframework.DocumentCollection<org.riverframework.domino.Document> {
	@Override
	public org.riverframework.domino.Database getDatabase();

	public int size();
	
	public DocumentCollection filter(String query);
	
	@Override
	public org.riverframework.domino.DocumentCollection removeAll();
}
