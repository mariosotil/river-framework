package org.riverframework.core;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.Counter;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.RiverException;
import org.riverframework.Session;
import org.riverframework.Unique;
import org.riverframework.View;

public class DefaultCounter extends DefaultDocument implements Counter, Unique {
	protected DefaultCounter(Database d, org.riverframework.module.Document _d) {
		super(d, _d);
	}

	protected final static String FORM_NAME = Session.ELEMENT_PREFIX + "counter";
	protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";
	protected final static String FIELD_COUNT = Session.FIELD_PREFIX + "count";

	protected View index = null;

	public static String getIndexName() {
		return Session.ELEMENT_PREFIX + "Counter_Index";
	}

	@Override
	protected Counter afterCreate() {
		setField("Form", FORM_NAME)
				.setField(FIELD_COUNT, 0);

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
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
