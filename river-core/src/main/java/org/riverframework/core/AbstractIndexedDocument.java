package org.riverframework.core;

import org.riverframework.wrapper.Document;

public abstract class AbstractIndexedDocument<T extends AbstractIndexedDocument<T>> extends AbstractDocument<T>
		implements IndexedDocument<T> {
	protected View index = null;

	protected String[] indexName = null;
	protected String idField = null;

	protected AbstractIndexedDocument(Database database, Document<?> _doc) {
		super(database, _doc);
	}

	@Override
	public IndexedDatabase getDatabase() {
		return (IndexedDatabase) super.getDatabase();
	}

	@Override
	public final View getIndex() {
		if (index == null) {
			index = getDatabase().getView(indexName);
		}
		return index;
	}

	@Override
	public final String getId() {
		return _doc.getFieldAsString(idField);
	}

	@Override
	public abstract T generateId();

	@Override
	public final T setId(String arg0) {
		_doc.setField(idField, arg0);
		return getThis();
	}

	// @Override
	// public <U extends AbstractDocument<?>> U getAs(Class<U> clazz) {
	// U doc = null;
	//
	// if (!IndexedDocument.class.isAssignableFrom(clazz))
	// return super.getAs(clazz);
	//
	// try {
	// Constructor<?> constructor =
	// clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
	// constructor.setAccessible(true);
	// doc = clazz.cast(constructor.newInstance(getDatabase(), _doc));
	// } catch (Exception e) {
	// throw new RiverException(e);
	// }
	//
	// return doc;
	// }

	@Override
	protected abstract T getThis();

}