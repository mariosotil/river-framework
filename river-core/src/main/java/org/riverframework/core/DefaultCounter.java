package org.riverframework.core;

import org.riverframework.extended.AbstractDocument;
import org.riverframework.extended.Counter;
import org.riverframework.extended.Unique;

/**
 * It is used to manage counters into the Database. This counters can be used to
 * create unique Ids.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultCounter extends AbstractDocument<DefaultCounter> 
implements Counter, Unique<DefaultCounter> {

	protected final static String FORM_NAME = Session.ELEMENT_PREFIX + "counter";
	protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";
	protected final static String FIELD_COUNT = Session.FIELD_PREFIX + "count";

	protected View index = null;

	protected DefaultCounter(Document doc) {
		super(doc);
	}

	@Override
	public String getIndexName() {
		return Session.ELEMENT_PREFIX + "Counter_Index";
	}

	protected DefaultCounter afterCreate() {
		doc.setField("Form", FORM_NAME).setField(FIELD_COUNT, 0);

		return this;
	}

	@Override
	public DefaultCounter setId(String id) {
		doc.setField(FIELD_ID, id);
		return this;
	}

	@Override
	public String getId() {
		String id = doc.getFieldAsString(FIELD_ID);
		return id;
	}

	@Override
	public DefaultCounter generateId() {
		// Do nothing
		return this;
	}

	@Override
	synchronized public long getCount() {
		long n = 0;

		// if (!isOpen())
		// throw new RiverException("The counter is not open.");

		n = doc.getFieldAsInteger(FIELD_COUNT) + 1;
		doc.setField(FIELD_COUNT, n).save();

		return n;
	}

	@Override
	protected DefaultCounter getThis() {
		return this;
	}
}