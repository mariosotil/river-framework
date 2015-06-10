package org.riverframework.core;

import org.riverframework.ClosedObjectException;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentIterator;

/**
 * It is a collection of Documents. Uses all the operations from ArrayList and
 * allows only objects that implements the Document interface.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractDocumentIterator implements DocumentIterator {
	protected Database database;
	protected org.riverframework.wrapper.DocumentIterator _iterator;

	protected AbstractDocumentIterator(Database d, org.riverframework.wrapper.DocumentIterator _iterator) {
		this.database = d;
		this._iterator = _iterator;
	}

	@Override
	public Database getDatabase() {
		return database;
	}

	@Override
	public Document next() {
		org.riverframework.wrapper.Document _doc = _iterator.next();
		org.riverframework.Document doc = database.getDocument(_doc);
		return doc;
	}

	@Override
	public boolean hasNext() {
		return _iterator.hasNext();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public DocumentIterator iterator() {
		return this;
	}

	@Override
	public boolean isOpen() {
		return (_iterator != null);
	}

	@Override
	public String getObjectId() {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		String result = _iterator.getObjectId();
		return result;
	}

	@Override
	public org.riverframework.wrapper.DocumentIterator getWrapperObject() {
		return _iterator;
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + getWrapperObject().toString() + ")";
	}

	@Override
	public DocumentIterator deleteAll() {
		_iterator.deleteAll();
		return this;
	}

	@Override
	public void close() {
		_iterator.close();
	}
}
