package org.riverframework.core;

import java.lang.reflect.Constructor;

import org.riverframework.ClosedObjectException;
import org.riverframework.RiverException;

/**
 * It is used to access Views of documents. Works as an index that makes easier
 * to access to documents.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractView<T extends AbstractView<T>> implements View {
	private Database database = null;
	protected org.riverframework.wrapper.View<?> _view = null;

	protected AbstractView(Database database,
			org.riverframework.wrapper.View<?> _view) {
		this.database = database;
		this._view = _view;
	}

	protected abstract T getThis();

	@Override
	public String getObjectId() {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		return _view.getObjectId();
	}

	@Override
	public org.riverframework.wrapper.View<?> getWrapperObject() {
		return _view;
	}

	@Override
	public Object getNativeObject() {
		return _view.getNativeObject();
	}

	@Override
	public Database getDatabase() {
		return database;
	}

	@Override
	public Document getDocumentByKey(String key) {
		org.riverframework.wrapper.Document<?> _doc = _view
				.getDocumentByKey(key);
		Document doc = new DefaultDocument(database, _doc);

		return doc;
	}

	@Override
	public <U extends AbstractDocument<?>> U getDocumentByKey(Class<U> clazz,
			String key) {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.Document<?> _doc = _view
				.getDocumentByKey(key);
		U doc = null;

		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(
					Database.class, org.riverframework.wrapper.Document.class);
			constructor.setAccessible(true);
			doc = clazz.cast(constructor.newInstance(database, _doc));
		} catch (Exception e) {
			throw new RiverException(e);
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

		org.riverframework.wrapper.DocumentIterator<?, ?> _iterator = _view
				.getAllDocuments();
		DocumentIterator result = new DefaultDocumentIterator(database,
				_iterator);

		return result;
	}

	@Override
	public DocumentIterator getAllDocumentsByKey(Object key) {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentIterator<?, ?> _iterator = _view
				.getAllDocumentsByKey(key);
		DocumentIterator result = new DefaultDocumentIterator(database,
				_iterator);

		return result;
	}

	@Override
	public T refresh() {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		_view.refresh();

		return getThis();
	}

	@Override
	public void delete() {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		_view.delete();
	}

	@Override
	public DocumentIterator search(String query) {
		DocumentIterator result = search(query, 0);
		return result;
	}

	@Override
	public DocumentIterator search(String query, int max) {
		if (!isOpen())
			throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentIterator<?, ?> _it = _view.search(
				query, max);
		DocumentIterator result = new DefaultDocumentIterator(database, _it);
		return result;
	}

	@Override
	public T addColumn(String name, String value, boolean isSorted) {
		_view.addColumn(name, value, isSorted);
		return getThis();
	}

	@Override
	public void close() {
		_view.close();
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + getWrapperObject().toString() + ")";
	}
}