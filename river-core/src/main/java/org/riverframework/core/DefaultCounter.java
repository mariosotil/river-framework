package org.riverframework.core;

import org.riverframework.RiverException;

/**
 * It is used to manage counters into the Database. This counters can be used to create unique Ids.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultCounter extends AbstractIndexedDocument<DefaultCounter> {
	final static String INDEX_NAME = Session.ELEMENT_PREFIX + "counter";
	final static String FORM_NAME = Session.ELEMENT_PREFIX + "counter";
	final static String FIELD_ID = Session.FIELD_PREFIX + "id";

	protected final static String FIELD_COUNT = Session.FIELD_PREFIX + "count";

	protected DefaultCounter(Database database, org.riverframework.wrapper.Document<?> _doc) {
		super(database, _doc);
	}

	@Override
	protected String getIdField() {
		return FIELD_ID;
	}

	@Override
	protected String getIndexName() {
		return INDEX_NAME;
	}

	@Override
	public View createIndex() {
		View index = getDatabase().createView(DefaultView.class, "Form=\"" + getIndexName() + "\"")
									.addColumn("Id", getIdField(), true);
		return index;
	}

	@Override
	public DefaultCounter afterCreate() {
		// TODO: this works only for IBM Notes. It's necessary to fix this. SQL++?
		_doc.setField("Form", FORM_NAME)
			.setField(FIELD_COUNT, 0);

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
		_doc.setField(FIELD_COUNT, n)
			.save();

		return n;
	}

	@Override
	protected DefaultCounter getThis() {
		return this;
	}
}