package org.riverframework.lotusnotes;

import org.riverframework.RiverException;

public class DefaultDocumentCollection implements
		org.riverframework.lotusnotes.DocumentCollection {
	protected Database rDatabase;
	protected lotus.domino.DocumentCollection col = null;
	protected lotus.domino.Document doc = null;

	public DefaultDocumentCollection(Database d, lotus.domino.DocumentCollection c) {
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
		lotus.domino.Document current = doc;
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
