package org.riverframework.domino;

public class DefaultDocumentCollection implements org.riverframework.domino.DocumentCollection {
	protected Database rDatabase;
	protected org.openntf.domino.DocumentCollection col = null;
	protected org.openntf.domino.Document doc = null;

	public DefaultDocumentCollection(Database d, org.openntf.domino.DocumentCollection c) {
		rDatabase = d;
		col = c;
		initIterator();
	}

	protected void initIterator() {
		doc = col.getFirstDocument();
	}

	@Override
	public int size(){
		return col.size();
	}

	@Override
	public boolean hasNext() {
		return doc != null;
	}

	@Override
	public org.riverframework.domino.Document next() {
		org.openntf.domino.Document current = doc;
		doc = col.getNextDocument(doc);
		Document rDoc = rDatabase.getDocument(current);
		return rDoc;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void filter(String query) {
		col.FTSearch(query);
	}
	
	@Override
	public DocumentCollection removeAll() {
		col.removeAll(true);
		return this;
	}
}
