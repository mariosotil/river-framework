package org.riverframework.core;

import org.riverframework.RiverException;

/**
 * It is used to manage counters into the Database. This counters can be used to create unique Ids.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultCounter extends AbstractIndexedDocument<DefaultCounter> {
	protected final static String FIELD_COUNT = Session.FIELD_PREFIX + "count";

	protected static View index = null;

	protected DefaultCounter(Database database, org.riverframework.wrapper.Document<?> _doc) {
		super(database, _doc);

		indexName = new String[] { AbstractIndexedDatabase.INDEX_NAME };
		idField = AbstractIndexedDatabase.FIELD_ID;
	}

	@Override
	public DefaultCounter afterCreate() {
		// TODO: this works only for IBM Notes. It's necessary to fix this.
		_doc.setField("Form", AbstractIndexedDatabase.FORM_NAME).setField(FIELD_COUNT, 0);

		return getThis();
	}

	@Override
	public DefaultCounter generateId() {
		// Do nothing
		return this;
	}

	synchronized public long getCount() {
		if (!isOpen())
			throw new RiverException("The DefaultCounter object is not open.");

		long n = _doc.getFieldAsInteger(FIELD_COUNT) + 1;
		_doc.setField(FIELD_COUNT, n).save();

		return n;
	}

	@Override
	protected DefaultCounter getThis() {
		return this;
	}
}