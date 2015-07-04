package org.riverframework.core;

import java.lang.reflect.Constructor;

import org.riverframework.RiverException;

/**
 * It is used to access Views of documents. Works as an index that makes easier access to the documents. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultView implements View {
	protected Database database = null;
	protected org.riverframework.wrapper.View<?> _view = null;

	protected DefaultView(Database d, org.riverframework.wrapper.View<?> obj) {
		database = d;
		_view = obj;
	}

	@Override
	public String getObjectId() {
		// if (!isOpen())
		// throw new ClosedObjectException("The View object is closed.");
		return _view.getObjectId();
	}

	@Override
	public org.riverframework.wrapper.View<?> getWrapperObject() {
		return _view;
	}

	@Override
	public Database getDatabase() {
		return database;
	}

	@Override
	public Document getDocumentByKey(String key) {
		org.riverframework.wrapper.Document<?> _doc = _view.getDocumentByKey(key);
		Document doc = new DefaultDocument(database, _doc);

		return doc;
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDocument<?>> U getDocumentByKey(Class<U> clazz, String key) {
		// if (!isOpen())
		// throw new ClosedObjectException("The View object is closed.");

		Document doc = getDocumentByKey(key); 
		U xDoc = null;
		
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(Document.class);
			xDoc = clazz.cast(constructor.newInstance(doc));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return xDoc;
	}

	@Override
	public boolean isOpen() {
		return _view != null && _view.isOpen();
	}

	@Override
	public DocumentIterator getAllDocuments() {
		// if (!isOpen())
		// throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentIterator<?> _iterator = _view.getAllDocuments();
		DocumentIterator result = new DefaultDocumentIterator(database, _iterator);

		return result;
	}

	@Override
	public DocumentIterator getAllDocumentsByKey(Object key) {
		// if (!isOpen())
		// throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentIterator<?> _iterator = _view.getAllDocumentsByKey(key);
		DocumentIterator result = new DefaultDocumentIterator(database, _iterator);

		return result;
	}

	@Override
	public View refresh() {
		// if (!isOpen())
		// throw new ClosedObjectException("The View object is closed.");

		_view.refresh();
		return this;
	}

	@Override
	public void delete() {
		// if (!isOpen())
		// throw new ClosedObjectException("The View object is closed.");

		_view.delete();
	}

	@Override
	public DocumentIterator search(String query) {
		// if (!isOpen())
		// throw new ClosedObjectException("The View object is closed.");

		org.riverframework.wrapper.DocumentIterator<?> _it = _view.search(query);
		DocumentIterator result = new DefaultDocumentIterator(database, _it);
		return result;
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