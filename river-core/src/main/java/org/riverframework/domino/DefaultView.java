package org.riverframework.domino;

import java.lang.reflect.Constructor;

import lotus.domino.NotesException;

import org.riverframework.RiverException;

public class DefaultView implements org.riverframework.domino.View {
	protected Database database = null;
	protected lotus.domino.View _view = null;
	protected lotus.domino.Document _doc = null;

	protected DefaultView(Database d, lotus.domino.View obj) {
		database = d;
		_view = obj;
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
		lotus.domino.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (DefaultDocument.class.isAssignableFrom(clazz)) {

			try {
				doc = _view.getDocumentByKey(key, true);
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, lotus.domino.Document.class);
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
		lotus.domino.ViewEntryCollection _col;
		try {
			_col = _view.getAllEntries();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		DocumentCollection result = new DefaultDocumentCollection(database).loadFrom(_col);

		return result;
	}

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key) {
		lotus.domino.DocumentCollection _col;
		try {
			_col = _view.getAllDocumentsByKey(key, true);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		DocumentCollection result = new DefaultDocumentCollection(database).loadFrom(_col);

		return result;
	}

	@Override
	public View refresh() {
		try {
			_view.refresh();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public org.riverframework.domino.DocumentCollection search(String query) {
		try {
			_view.FTSearch(query);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		DocumentCollection result = new DefaultDocumentCollection(database).loadFrom(_view);

		return result;
	}
}
