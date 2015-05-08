package org.riverframework.core;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentIterator;

/**
 * It is a collection of Documents. Uses all the operations from ArrayList and allows only objects that
 * implements the Document interface. 
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
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
