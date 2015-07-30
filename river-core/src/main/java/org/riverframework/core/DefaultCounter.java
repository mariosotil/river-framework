package org.riverframework.core;

import org.riverframework.RiverException;

/**
 * It is used to manage counters into the Database. This counters can be used to
 * create unique Ids.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultCounter extends
		AbstractIndexedDocument<DefaultCounter> {

	protected final static String FIELD_COUNT = Session.FIELD_PREFIX + "count";

	protected DefaultCounter(Database database,
			org.riverframework.wrapper.Document<?> _doc) {
		super(database, _doc);
	}

	@Override
	public String getIdField() {
		return Session.FIELD_PREFIX + "id";
	}

	@Override
	public String getIndexName() {
		return Session.ELEMENT_PREFIX + "counter";
	}

	@Override
	public String getTableName() {
		return Session.ELEMENT_PREFIX + "counter";
	}

	@Override
	protected String[] getParametersToCreateIndex() {
		// TODO: this works only for IBM Notes. It MUST be fixed in the next
		// version
		return new String[] { getIndexName(), "Form=\"" + getTableName() + "\"" };
	}

	@Override
	public DefaultCounter afterCreate() {
		setField(FIELD_COUNT, 0);

		return this;
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