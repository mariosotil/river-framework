package org.riverframework.core;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.ClosedObjectException;
import org.riverframework.Database;
import org.riverframework.DocumentIterator;
import org.riverframework.DocumentList;
import org.riverframework.RiverException;
import org.riverframework.View;

/**
 * Implements the View interface. Groups the Documents using an internal index
 * and allows access to them searching by key or just getting all the documents.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractView implements org.riverframework.View {
	protected Database database = null;
	protected org.riverframework.wrapper.View _view = null;
	protected org.riverframework.wrapper.Document _doc = null;

	protected AbstractView(Database d, org.riverframework.wrapper.View obj) {
		database = d;
		_view = obj;
	}

	@Override
	public String getObjectId() {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");
		return _view.getObjectId();
	}

	@Override
	public org.riverframework.wrapper.View getWrapperObject() {
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
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		U doc = null;
		org.riverframework.wrapper.Document _doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (AbstractDocument.class.isAssignableFrom(clazz)) {
			_doc = _view.getDocumentByKey(key);

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
				doc = clazz.cast(constructor.newInstance(database, _doc));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		if (doc == null) {
			doc = clazz.cast(new DefaultDocument(database, null));
		}

		return doc;
	}

	@Override
	public boolean isOpen() {
		return _view != null && _view.isOpen();
	}

	@Override
	public DocumentIterator getAllDocuments() {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentIterator _iterator = _view.getAllDocuments();
		DocumentIterator result = new DefaultDocumentIterator(database, _iterator);

		return result;
	}

	@Override
	public DocumentIterator getAllDocumentsByKey(Object key) {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentIterator _iterator = _view.getAllDocumentsByKey(key);
		DocumentIterator result = new DefaultDocumentIterator(database, _iterator);

		return result;
	}

	@Override
	public View refresh() {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		_view.refresh();
		return this;
	}

	@Override
	public void delete() {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		_view.delete();
	}

	@Override
	public org.riverframework.DocumentList search(String query) {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentList _col = _view.search(query);
		DocumentList result = new DefaultDocumentList(database, _col);
		return result;
	}

	@Override
	public void close() {
		_doc.close();
		_view.close();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
