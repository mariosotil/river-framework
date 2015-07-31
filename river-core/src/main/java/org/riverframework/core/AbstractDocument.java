package org.riverframework.core;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.riverframework.ClosedObjectException;
import org.riverframework.RiverException;

/**
 * It is used to manage Documents. It is used if we don't need to create
 * specific classes for each document type.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractDocument<T extends AbstractDocument<T>>
		implements Document {
	private Database database = null;
	protected org.riverframework.wrapper.Document<?> _doc = null;
	boolean isModified = false;

	protected AbstractDocument(Database database,
			org.riverframework.wrapper.Document<?> _doc) {
		this.database = database;
		this._doc = _doc;

		if (_doc != null && _doc.isOpen() && _doc.isNew()) {
			_doc.setTable(getTableName());
		}

		isModified = false;
	}

	protected abstract T getThis();

	/**
	 * @return the table where the document belongs. If this method is not
	 *         overrided, it will returns the simple name of the current class.
	 */
	@Override
	public String getTableName() {
		return "{" + this.getClass().getSimpleName() + "}";
	}

	/**
	 * Changes the "modified" flag. This method has to be used only if
	 * necessary.
	 * 
	 * @param m
	 *            the new state
	 * @return this object
	 */
	protected T setModified(boolean m) {
		isModified = m;
		return getThis();
	}

	/**
	 * Allows to define tasks to refresh Document's fields
	 * 
	 * @return this object
	 */
	protected T internalRecalc() {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		_doc.recalc();
		return getThis();
	}

	@Override
	public String getObjectId() {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		String result = _doc.getObjectId();
		return result;
	}

	@Override
	public org.riverframework.wrapper.Document<?> getWrapperObject() {
		return _doc;
	}

	@Override
	public Object getNativeObject() {
		return _doc.getNativeObject();
	}

	@Override
	public Database getDatabase() {
		return database;
	}

	/**
	 * Compares two vectors and determines if they are numeric equals,
	 * independent of its type.
	 * 
	 * @param vector1
	 *            the first vector
	 * @param vector2
	 *            the second vector
	 * @return true if the vectors are numeric equals
	 */
	protected static boolean numericEquals(Field vector1, Field vector2) {
		if (vector1.size() != vector2.size())
			return false;
		if (vector1.isEmpty())
			return true;

		Iterator<Object> it1 = vector1.iterator();
		Iterator<Object> it2 = vector2.iterator();

		while (it1.hasNext()) {
			Object obj1 = it1.next();
			Object obj2 = it2.next();

			if (!(obj1 instanceof Number && obj2 instanceof Number))
				return false;

			if (((Number) obj1).doubleValue() != ((Number) obj2).doubleValue())
				return false;
		}

		return true;
	}

	/**
	 * Verifies if the new value is different from the field's old value. It's
	 * useful, for example, in NoSQL databases that replicates data between
	 * servers. This verification prevents to mark a field as modified and to be
	 * replicated needlessly.
	 * 
	 * @param field
	 *            the field that we are checking before to be modified
	 * @param value
	 *            the new value
	 * @return true if the new and the old values are different and the value
	 *         was changed.
	 */
	protected boolean setFieldIfNecessary(String field, Object value) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		if (!compareFieldValue(field, value)) {
			_doc.setField(field, value);
			return true;
		}

		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean compareFieldValue(String field, Object value) {

		boolean equal = false;

		Field oldValues = getField(field);
		Field newValues = null;

		if (value instanceof java.util.Vector) {
			newValues = new DefaultField((java.util.Vector) value);
		} else if (value instanceof Object[]) {
			newValues = new DefaultField();
			for (Object o : (Object[]) value) {
				newValues.add(o);
			}
		} else {
			newValues = new DefaultField();
			newValues.add(value);
		}

		if (value instanceof Number[] || value instanceof Number) {
			equal = numericEquals(newValues, oldValues);
		} else {
			equal = oldValues.equals(newValues);
		}

		return equal;
	}

	@Override
	public T setField(String field, Object value) {
		isModified = setFieldIfNecessary(field, value) || isModified;
		return getThis();
	}

	@Override
	public Map<String, Field> getFields() {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		Map<String, Field> result = _doc.getFields();
		return result;
	}

	@Override
	public Field getField(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		Field value = _doc.getField(field);
		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		String result = _doc.getFieldAsString(field);
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		int result = _doc.getFieldAsInteger(field);
		return result;
	}

	@Override
	public long getFieldAsLong(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		long result = _doc.getFieldAsLong(field);
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		double result = _doc.getFieldAsDouble(field);
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		Date result = _doc.getFieldAsDate(field);
		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		return _doc.isFieldEmpty(field);
	}

	@Override
	public <U extends AbstractDocument<?>> U getAs(Class<U> clazz) {
		U doc = null;

		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(
					Database.class, org.riverframework.wrapper.Document.class);
			constructor.setAccessible(true);
			doc = clazz.cast(constructor.newInstance(database, _doc));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return doc;
	}

	@Override
	public boolean isModified() {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		return isModified;
	}

	@Override
	public boolean isOpen() {
		return _doc != null && _doc.isOpen();
	}

	@Override
	public boolean isNew() {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		boolean result = _doc.isNew();
		return result;
	}

	@Override
	public T delete() {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		_doc.delete();
		return getThis();
	}

	@Override
	public T save(boolean force) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		if (force || isModified) {
			_doc.save();
			isModified = false;
		}

		return getThis();
	}

	@Override
	public T save() {
		return save(true);
	}

	@Override
	public boolean hasField(String field) {
		if (!isOpen())
			throw new ClosedObjectException("The Document object is closed.");

		boolean result = _doc.hasField(field);
		return result;
	}

	@Override
	public T afterCreate() {
		return getThis();
	}

	@Override
	public Document recalc() {
		return internalRecalc();
	}

	@Override
	public void close() {
		_doc.close();
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + getWrapperObject().toString() + ")";
	}
}
