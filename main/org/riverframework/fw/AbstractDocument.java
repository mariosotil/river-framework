package org.riverframework.fw;

import java.util.Iterator;
import java.util.Vector;

import org.riverframework.RiverException;

/*
 * Loads an IBM Notes document
 * <p>
 * This is a javadoc test
 * 
 *  @author mario.sotil@gmail.com
 *  @version 0.0.x
 */

/*
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public abstract class AbstractDocument<T> implements org.riverframework.Document<T> {
	protected org.riverframework.Database<?> rDatabase = null;
	// protected org.riverframework.View rIndex = null;
	// protected void loadIndex() {
	// rIndex = rDatabase.getView(Context.PREFIX + "_" + this.getClass().getSimpleName() +
	// "_Index");
	// }

	protected T document = null;
	private boolean isModified = false;

	public AbstractDocument(org.riverframework.Database<?> d) {
		rDatabase = d;
		isModified = false;
	}

	public AbstractDocument(org.riverframework.Database<?> d, T doc) {
		rDatabase = d;
		document = doc;
		isModified = false;
	}

	/*
	 * protected static boolean numericEquals(Vector<? extends Number> c1, Vector<? extends Number> c2) {
	 * if (c1.size() != c2.size())
	 * return false;
	 * if (c1.isEmpty())
	 * return true;
	 * 
	 * Iterator<? extends Number> it1 = c1.iterator();
	 * Iterator<? extends Number> it2 = c2.iterator();
	 * 
	 * while (it1.hasNext()) {
	 * if (it1.next().doubleValue() != it2.next().doubleValue())
	 * return false;
	 * }
	 * 
	 * return true;
	 * }
	 */

	protected static boolean numericEquals(Vector<Object> c1, Vector<Object> c2) {
		if (c1.size() != c2.size())
			return false;
		if (c1.isEmpty())
			return true;

		Iterator<Object> it1 = c1.iterator();
		Iterator<Object> it2 = c2.iterator();

		while (it1.hasNext()) {
			if (((Number) it1.next()).doubleValue() != ((Number) it2.next()).doubleValue())
				return false;
		}

		return true;
	}

	@Override
	public abstract String getUniversalId();

	protected abstract org.riverframework.Document<T> internalRecalc();

	protected abstract org.riverframework.Document<T> afterCreate();

	protected abstract boolean replaceFieldValue(String field, Object value);

	protected boolean setFieldIfNecessary(String field, Object value) {
		try {
			if (!compareFieldValue(field, value)) {
				replaceFieldValue(field, value);
				return true;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean compareFieldValue(String field, Object value) {

		boolean equal = false;

		try {
			Vector<Object> oldValues = getField(field);
			Vector<Object> newValues = null;

			if (value instanceof java.util.Vector) {
				newValues = (Vector<Object>) value;
			} else if (value instanceof Object[]) {
				newValues = new Vector<Object>();
				for (Object o : (Object[]) value) {
					newValues.add(o);
				}
			} else {
				newValues = new Vector<Object>();
				newValues.add(value);
			}

			if (value instanceof Number[] || value instanceof Number) {
				equal = numericEquals(newValues, oldValues);
			} else {
				equal = oldValues.equals(newValues);
			}

		} catch (Exception e) {
			throw new RiverException(e);
		}

		return equal;
	}

	@Override
	public org.riverframework.Document<T> setField(String field, Object value) {
		isModified = setFieldIfNecessary(field, value) || isModified;
		return this;
	}

	@Override
	public abstract Vector<Object> getField(String field);

	@Override
	public abstract String getFieldAsString(String field);

	@Override
	public abstract int getFieldAsInteger(String field);

	@Override
	public abstract double getFieldAsDouble(String field);

	@Override
	public abstract java.util.Date getFieldAsDate(String field);

	@Override
	public abstract boolean isFieldEmpty(String field);

	@Override
	public boolean isModified() {
		return isModified;
	}

	@Override
	public boolean isOpen() {
		return document != null;
	}

	@Override
	public abstract boolean isNew();

	@Override
	public abstract org.riverframework.Document<T> remove();

	protected abstract org.riverframework.Document<T> saveImplementation();

	@Override
	public org.riverframework.Document<T> save(boolean force) {
		try {
			if (force || isModified) {
				saveImplementation();
				isModified = false;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public org.riverframework.Document<T> save() {
		save(true);
		return this;
	}

	protected void close() {
		try {
			if (isOpen()) {
				document = null;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}

	@Override
	public org.riverframework.Document<T> recalc() {
		return internalRecalc();
	}

	@Override
	public org.riverframework.Database<?> getDatabase() {
		return rDatabase;
	}
}
