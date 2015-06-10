package org.riverframework.core;

import org.riverframework.Counter;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.RiverException;
import org.riverframework.Session;
import org.riverframework.Unique;
import org.riverframework.View;

/**
 * It is used to manage counters into the Database. This counters can be used to
 * create unique Ids.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultCounter extends AbstractDocument<DefaultCounter> implements Counter, Unique {
	protected DefaultCounter(Database d, org.riverframework.wrapper.Document _d) {
		super(d, _d);
	}

	protected final static String FORM_NAME = Session.ELEMENT_PREFIX + "counter";
	protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";
	protected final static String FIELD_COUNT = Session.FIELD_PREFIX + "count";

	protected View index = null;

	@Override
	public String getIndexName() {
		return Session.ELEMENT_PREFIX + "Counter_Index";
	}

	@Override
	protected DefaultCounter afterCreate() {
		setField("Form", FORM_NAME).setField(FIELD_COUNT, 0);

		return this;
	}

	@Override
	public Document setId(String id) {
		setField(FIELD_ID, id);
		return this;
	}

	@Override
	public String getId() {
		String id = getFieldAsString(FIELD_ID);
		return id;
	}

	@Override
	public Document generateId() {
		// Do nothing
		return this;
	}

	@Override
	public long getCount() {
		// TODO: synchronize this and test it
		long n = 0;

		if (!isOpen())
			throw new RiverException("The counter is not open.");

		n = getFieldAsInteger(FIELD_COUNT) + 1;
		setField(FIELD_COUNT, n).save();

		return n;
	}

	@Override
	protected DefaultCounter getThis() {
		return this;
	}
}