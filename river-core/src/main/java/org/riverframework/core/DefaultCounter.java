package org.riverframework.core;

import org.riverframework.RiverException;

/**
 * It is used to manage counters into the Database. This counters can be used to create unique Ids.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultCounter extends AbstractDocument<DefaultCounter> implements IndexedDocument<DefaultCounter> {
	protected final static String INDEX_NAME = Session.ELEMENT_PREFIX + "counter";
	protected final static String FORM_NAME = Session.ELEMENT_PREFIX + "counter";
	protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";
	protected final static String FIELD_COUNT = Session.FIELD_PREFIX + "count";
	protected static View index = null;

	protected DefaultCounter(Database database, org.riverframework.wrapper.Document<?> _doc) {
		super(database, _doc);

		if (index == null) {
			// TODO: you cannot always hope that a view is loaded with one unique parameter.
			index = database.getView(INDEX_NAME);
		}
	}

	@Override
	public View getIndex() {
		return index;
	}

	@Override
	public DefaultCounter afterCreate() {
		// TODO: this works only for IBM Notes. It's necessary to fix this.
		_doc.setField("Form", FORM_NAME).setField(FIELD_COUNT, 0);

		return getThis();
	}

	@Override
	public DefaultCounter setId(String id) {
		_doc.setField(FIELD_ID, id);
		return this;
	}

	@Override
	public String getId() {
		String id = _doc.getFieldAsString(FIELD_ID);
		return id;
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