package org.riverframework.lotusnotes;

import lotus.domino.DocumentCollection;

import org.riverframework.RiverException;

/*
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public class DefaultDocumentCollection extends
		org.riverframework.fw.AbstractDocumentCollection<lotus.domino.DocumentCollection> {
	protected lotus.domino.Document doc = null;

	public DefaultDocumentCollection(DefaultDatabase d, DocumentCollection c) {
		super(d, c);
	}

	@Override
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
	public org.riverframework.lotusnotes.DefaultDocument next() {
		lotus.domino.Document current = doc;
		try {
			doc = col.getNextDocument(doc);
		} catch (Exception e) {
			throw new RiverException(e);
		}
		org.riverframework.lotusnotes.DefaultDocument rDoc = new DefaultDocument(
				(DefaultDatabase) rDatabase, current);
		return rDoc;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
