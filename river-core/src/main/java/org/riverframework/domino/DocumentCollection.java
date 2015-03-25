package org.riverframework.domino;

public interface DocumentCollection extends org.riverframework.DocumentCollection<org.riverframework.domino.Document> {
	@Override
	public org.riverframework.domino.Database getDatabase();

	public org.riverframework.domino.DocumentCollection loadFrom(lotus.domino.DocumentCollection _col);

	public org.riverframework.domino.DocumentCollection loadFrom(lotus.domino.ViewEntryCollection _col);
	
	public org.riverframework.domino.DocumentCollection loadFrom(lotus.domino.View _view);
	
	@Override
	public org.riverframework.domino.DocumentCollection deleteAll();
}
