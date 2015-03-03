package org.riverframework.lotusnotes;

import lotus.domino.View;

import org.riverframework.RiverException;

/*
 * Should I change this class' name to "Index"?
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public class DefaultView extends org.riverframework.fw.AbstractView<lotus.domino.View> {
	lotus.domino.Document iteratorDoc = null;

	protected DefaultView(DefaultDatabase d, View obj) {
		super(d, obj);
	}

	@Override
	public org.riverframework.lotusnotes.DefaultDocument getDocumentByKey(Object key) {
		org.riverframework.lotusnotes.DefaultDocument rDoc = null;

		try {
			// Always exact match
			lotus.domino.Document doc = view.getDocumentByKey(key, true);
			rDoc = new DefaultDocument((DefaultDatabase) rDatabase, doc);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDoc;
	}

	@Override
	public org.riverframework.lotusnotes.DefaultDocumentCollection getAllDocumentsByKey(Object key) {
		org.riverframework.lotusnotes.DefaultDocumentCollection rDocumentIterator = null;

		try {
			lotus.domino.DocumentCollection col = view.getAllDocumentsByKey(key, true); // Always exact match
			rDocumentIterator = new org.riverframework.lotusnotes.DefaultDocumentCollection(
					(DefaultDatabase) rDatabase, col);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDocumentIterator;
	}

	@Override
	public org.riverframework.lotusnotes.DefaultView refresh() {
		try {
			view.refresh();
		} catch (Exception e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	protected void close() {
		try {
			if (view != null) {
				view.recycle();
				view = null;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	/*
	 * Implementing Iterator
	 */

	@Override
	protected void initIterator() {
		try {
			if (view != null) {
				iteratorDoc = view.getFirstDocument();
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean hasNext() {
		return iteratorDoc != null;
	}

	@Override
	public org.riverframework.lotusnotes.DefaultDocument next() {
		lotus.domino.Document current = iteratorDoc;
		try {
			iteratorDoc = view.getNextDocument(iteratorDoc);
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
