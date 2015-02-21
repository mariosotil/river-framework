package org.riverframework.fw;

import org.riverframework.Session;
import org.riverframework.Unique;
import org.riverframework.development.Relationable;

public abstract class AbstractUnique<T> implements org.riverframework.Unique {
	/**
	 * This field will have the key used to generate the Id
	 */
	public static final String FIELD_ID_KEY = Session.PREFIX + "IdKey";
	/**
	 * This field will have the counter calculated for the key in FIELD_ID_KEY
	 */
	public static final String FIELD_ID_COUNTER = Session.PREFIX + "IdCounter";

	/**
	 * This variable will be the reference to the object that use this class by composition
	 */
	protected org.riverframework.Document<T> rDoc = null;

	public AbstractUnique(org.riverframework.Document<T> doc) {
		rDoc = doc;
	}

	public abstract int getCounter(String key);

	public abstract String getIdKey();

	public abstract String getIdComplete();

	@Override
	public Unique calcId() {
		String currentKey = rDoc.getFieldAsString(FIELD_ID_KEY);

		String newKey = getIdKey();
		String newCode = "";

		if (!currentKey.equals(newKey) || rDoc.isFieldEmpty(FIELD_ID_COUNTER)) {
			int newCounter = getCounter(rDoc.getClass().getSimpleName());

			rDoc.setField(FIELD_ID_KEY, newKey);
			rDoc.setField(FIELD_ID_COUNTER, newCounter);

			newCode = getIdComplete();
			rDoc.setField(org.riverframework.Unique.FIELD_ID, newCode);
		}

		if (this instanceof Relationable) {
			// Propagate to children
		}

		return this;
	}

	@Override
	public final String getId() {
		return rDoc.getFieldAsString(org.riverframework.Unique.FIELD_ID);
	}
}
