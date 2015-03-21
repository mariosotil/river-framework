package org.riverframework.domino;


public interface DocumentCollection extends org.riverframework.DocumentCollection<org.riverframework.domino.Document> {
	public int size();
	
	public void filter(String query);
	
	@Override
	public org.riverframework.domino.DocumentCollection removeAll();
}
