package org.riverframework.domino;

import java.lang.reflect.Constructor;

import org.riverframework.RiverException;

public class DefaultView implements org.riverframework.domino.View {
	protected Database rDatabase = null;
	protected org.openntf.domino.View view = null;
	org.openntf.domino.Document iteratorDoc = null;

	protected DefaultView(Database d, org.openntf.domino.View obj) {
		rDatabase = d;
		view = obj;

		initIterator();
	}

	@Override
	public <U extends org.riverframework.Document> U getDocumentByKey(Class<U> clazz, Object key) {
		U rDoc = null;
		org.openntf.domino.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (DefaultDocument.class.isAssignableFrom(clazz)) {

			doc = view.getDocumentByKey(key, true);

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				rDoc = clazz.cast(constructor.newInstance(rDatabase, doc));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		if (rDoc == null) {
			rDoc = clazz.cast(new DefaultDocument(rDatabase, null));
		} else {
			((DefaultDocument) rDoc).afterCreate().setModified(false);
		}

		return rDoc;
	}

	@Override
	public boolean isOpen() {
		return view != null;
	}

	@Override
	public DocumentCollection getAllDocuments() {
		org.openntf.domino.DocumentCollection col = view.getAllDocuments(); // Always exact match
		DocumentCollection result = new DefaultDocumentCollection(rDatabase, col);

		return result;
	}

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key) {
		org.openntf.domino.DocumentCollection col = view.getAllDocumentsByKey(key, true); // Always exact match
		DocumentCollection result = new DefaultDocumentCollection(rDatabase, col);

		return result;
	}

	@Override
	public View refresh() {
		view.refresh();
		return this;
	}

	/*
	 * Implementing Iterator
	 */
	protected void initIterator() {
		if (view != null) {
			iteratorDoc = view.getFirstDocument();
		}
	}

	@Override
	public boolean hasNext() {
		return iteratorDoc != null;
	}

	@Override
	public org.riverframework.domino.Document next() {
		org.openntf.domino.Document current = iteratorDoc;
		iteratorDoc = view.getNextDocument(iteratorDoc);
		Document rDoc = rDatabase.getDocument(current);
		return rDoc;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
