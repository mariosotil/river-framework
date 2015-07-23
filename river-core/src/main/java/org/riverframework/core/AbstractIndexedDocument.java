package org.riverframework.core;

import org.riverframework.wrapper.Document;

public abstract class AbstractIndexedDocument<T extends AbstractIndexedDocument<T>> 
extends AbstractDocument<T> implements IndexedDocument<T> {

	protected View index = null;

	protected String[] indexName = null;
	protected String indexField = null;

	protected AbstractIndexedDocument(Database database, Document<?> _doc) {
		super(database, _doc);
	}

	@Override
	public final View getIndex() {
		if (index == null) {
			index = database.getView(indexName);
		}
		return index;
	}

	@Override
	public final String getId() {
		return _doc.getFieldAsString(indexField);
	}

	@Override
	public abstract T generateId();

	@Override
	public final T setId(String arg0) {
		_doc.setField(indexField, arg0);
		return getThis();
	}

	@Override
	protected abstract T getThis();

}