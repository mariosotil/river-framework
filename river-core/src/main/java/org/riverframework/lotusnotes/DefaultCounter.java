package org.riverframework.lotusnotes;

import org.riverframework.RiverException;

public class DefaultCounter extends DefaultDocument implements Counter, Unique {
	protected final static String FORM_NAME = Session.PREFIX + "Counter";
	protected final static String FIELD_ID = Session.PREFIX + "Id";
	protected final static String FIELD_COUNT = Session.PREFIX + "Count";

	protected View index = null;

	public static String getIndexName() {
		return Session.PREFIX + "Counter_Index";
	}

	protected DefaultCounter(Database d, lotus.domino.Document doc) {
		super(d, doc);
	}

	@Override
	protected Counter afterCreate() {
		setForm(FORM_NAME)
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
		//Do nothing
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

}
