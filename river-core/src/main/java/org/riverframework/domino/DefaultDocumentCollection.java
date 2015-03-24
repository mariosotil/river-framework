package org.riverframework.domino;

public class DefaultDocumentCollection implements org.riverframework.domino.DocumentCollection {
	protected Database database;
	protected org.openntf.domino.DocumentCollection _col = null;
	protected org.openntf.domino.Document _doc = null;

	public DefaultDocumentCollection(Database d, org.openntf.domino.DocumentCollection c) {
		database = d;
		_col = c;
		initIterator();
	}

	@Override
	public org.riverframework.domino.Database getDatabase() {
		return database;
	}

	protected void initIterator() {
		_doc = _col.getFirstDocument();
	}

	@Override
	public int size(){
		return _col.size();
	}

	@Override
	public boolean hasNext() {
		return _doc != null;
	}

	@Override
	public org.riverframework.domino.Document next() {
		org.openntf.domino.Document current = _doc;
		_doc = _col.getNextDocument(_doc);
		Document rDoc = database.getDocument(current);
		return rDoc;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DocumentCollection filter(String query) {
		_col.FTSearch(query);
		return this;
	}
	
	@Override
	public DocumentCollection removeAll() {
		_col.removeAll(true);
		return this;
	}
}
