package org.riverframework.core;

import org.riverframework.wrapper.Document;

public abstract class AbstractIndexedDocument<T extends AbstractIndexedDocument<T>> extends AbstractDocument<T>
		implements IndexedDocument<T> {

	protected AbstractIndexedDocument(Database database, Document<?> _doc) {
		super(database, _doc);
	}

	protected abstract String getIdField();

	protected abstract String getIndexName();

	@Override
	public IndexedDatabase getDatabase() {
		return (IndexedDatabase) super.getDatabase();
	}

	@Override
	public abstract View createIndex();

	@Override
	public final View getIndex() {
		View index = getDatabase().getView(getIndexName());

		return (index != null && index.isOpen()) ? index : getDatabase().getClosedView();
	}

	@Override
	public final String getId() {
		return _doc.getFieldAsString(getIdField());
	}

	@Override
	public abstract T generateId();

	@Override
	public final T setId(String arg0) {
		_doc.setField(getIdField(), arg0);
		return getThis();
	}

	@Override
	protected abstract T getThis();

}