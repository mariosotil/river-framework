package org.riverframework.core;

import java.lang.reflect.Constructor;

import org.riverframework.Base;
import org.riverframework.Database;
import org.riverframework.DocumentCollection;
import org.riverframework.RiverException;
import org.riverframework.View;

public class DefaultView implements org.riverframework.View {
	protected Database database = null;
	protected org.riverframework.module.View _view = null;
	protected org.riverframework.module.Document _doc = null;

	protected DefaultView(Database d, org.riverframework.module.View obj) {
		database = d;
		_view = obj;
	}

	@Override
	public String getObjectId() {
		return ""; // _view.getObjectId();
	}

	@Override
	public Base getParent() {
		return database;
	}

	@Override
	public Object getWrappedObject() {
		return _view;
	}

	@Override
	public org.riverframework.Database getDatabase() {
		return database;
	}

	@Override
	public org.riverframework.Document getDocumentByKey(String key) {
		return getDocumentByKey(DefaultDocument.class, key);
	}

	@Override
	public <U extends org.riverframework.Document> U getDocumentByKey(Class<U> clazz, String key) {
		U rDoc = null;
		org.riverframework.module.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (DefaultDocument.class.isAssignableFrom(clazz)) {
			doc = _view.getDocumentByKey(key);

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.module.Document.class);
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
		return _view != null && _view.isOpen();
	}

	@Override
	public DocumentCollection getAllDocuments() {
		org.riverframework.module.DocumentCollection _col;
		_col = _view.getAllDocuments();
		DocumentCollection result = new DefaultDocumentCollection(database).loadFrom(_col);

		return result;
	}

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key) {
		org.riverframework.module.DocumentCollection _col;
		_col = _view.getAllDocumentsByKey(key);
		DocumentCollection result = new DefaultDocumentCollection(database).loadFrom(_col);

		return result;
	}

	@Override
	public View refresh() {
		_view.refresh();
		return this;
	}

	@Override
	public org.riverframework.DocumentCollection search(String query) {
		org.riverframework.module.DocumentCollection _col;
		_col = _view.search(query);
		DocumentCollection result = new DefaultDocumentCollection(database).loadFrom(_col);
		return result;
	}

	@Override
	public void close() {
		_doc.close();
		_view.close();
	}
}
