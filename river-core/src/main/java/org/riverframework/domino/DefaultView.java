package org.riverframework.domino;

import java.lang.reflect.Constructor;

import org.riverframework.RiverException;

public class DefaultView implements org.riverframework.domino.View {
	protected Database database = null;
	protected org.openntf.domino.View _view = null;
	protected org.openntf.domino.Document _doc = null;

	protected DefaultView(Database d, org.openntf.domino.View obj) {
		database = d;
		_view = obj;

		initIterator();
	}

	@Override
	public org.riverframework.domino.Database getDatabase() {
		return database;
	}

	@Override
	public org.riverframework.domino.Document getDocumentByKey(String key) {
		return getDocumentByKey(DefaultDocument.class, key);
	}

	@Override
	public <U extends org.riverframework.domino.Document> U getDocumentByKey(Class<U> clazz, String key) {
		U rDoc = null;
		org.openntf.domino.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (DefaultDocument.class.isAssignableFrom(clazz)) {

			doc = _view.getDocumentByKey(key, true);

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				rDoc = clazz.cast(constructor.newInstance(database, doc));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		if (rDoc == null) {
			rDoc = clazz.cast(new DefaultDocument(database, null));
		} else {
			((DefaultDocument) rDoc).afterCreate().setModified(false);
		}

		return rDoc;
	}

	@Override
	public boolean isOpen() {
		return _view != null;
	}

	@Override
	public DocumentCollection getAllDocuments() {
		org.openntf.domino.DocumentCollection col = _view.getAllDocuments(); // Always exact match
		DocumentCollection result = new DefaultDocumentCollection(database, col);

		return result;
	}

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key) {
		org.openntf.domino.DocumentCollection col = _view.getAllDocumentsByKey(key, true); // Always exact match
		DocumentCollection result = new DefaultDocumentCollection(database, col);

		return result;
	}

	@Override
	public View refresh() {
		_view.refresh();
		return this;
	}

	@Override
	public View filter(String query) {
		_view.FTSearch(query);
		return this;
	}

	/*
	 * Implementing Iterator
	 */
	protected void initIterator() {
		if (_view != null) {
			_doc = _view.getFirstDocument();
		}
	}

	@Override
	public boolean hasNext() {
		return _doc != null;
	}

	@Override
	public org.riverframework.domino.Document next() {
		org.openntf.domino.Document current = _doc;
		_doc = _view.getNextDocument(_doc);
		Document rDoc = database.getDocument(current);
		return rDoc;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
