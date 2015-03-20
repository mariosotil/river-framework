package org.riverframework.domino;

import org.riverframework.RiverException;

public class DefaultDocumentCollection implements
		org.riverframework.domino.DocumentCollection {
	protected Database rDatabase;
	protected org.openntf.domino.DocumentCollection col = null;
	protected org.openntf.domino.Document doc = null;

	public DefaultDocumentCollection(Database d, org.openntf.domino.DocumentCollection c) {
		try {
			rDatabase = d;
			col = c;
			initIterator();

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	protected void initIterator() {
		try {
			doc = col.getFirstDocument();
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean hasNext() {
		return doc != null;
	}

	@Override
	public Document next() {
		org.openntf.domino.Document current = doc;
		try {
			doc = col.getNextDocument(doc);
		} catch (Exception e) {
			throw new RiverException(e);
		}
		Document rDoc = new DefaultDocument(rDatabase, current);
		return rDoc;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
