package org.riverframework.core;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.ClosedObjectException;
import org.riverframework.Database;
import org.riverframework.Document;

/**
 * It is the implementation of the Document interface. It lets you manage its fields. 
 * 
 * @author mario.sotil@gmail.com
 *
 * @param <T> The class that inherits from AbstractDocument. It's used to make possible the method chaining. 
 */
public abstract class AbstractDocument <T extends AbstractDocument<T>> implements org.riverframework.Document {
	protected Database database = null;
	protected org.riverframework.module.Document _doc = null;
	protected boolean isModified = false;

	protected AbstractDocument(Database d, org.riverframework.module.Document _d) {
		database = d;
		_doc = _d;
		isModified = false;
	}

	/**
	 * Returns this object. It's used for make possible the method chaining.
	 * @return this object.
	 */
	protected abstract T getThis();
	
	/**
	 * Changes the "modified" flag. This method has to be used only if necessary.
	 * @param m the new state
	 * @return this object
	 */
	protected T setModified(boolean m) {
		isModified = m;
		return getThis();
	}
	
	/**
	 * Allows to define the tasks that has to be made just after a Document is created into a Database. 
	 * @return this object
	 */
	protected T afterCreate() {
		return getThis();
	}

	/**
	 * Allows to define tasks to refresh Document's fields
	 * @return this object
	 */
	protected T internalRecalc() {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		_doc.recalc();
		return getThis();
	}

	@Override
	public String getObjectId() {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		String result = _doc.getObjectId();
		return result;
	}

	@Override
	public Object getModuleObject() {
		return _doc;
	}

	@Override
	public org.riverframework.Database getDatabase() {
		return database;
	}

	/**
	 * Compares two vectors and determines if they are numeric equals, independent of its type.
	 * @param vector1 the first vector
	 * @param vector2 the second vector
	 * @return true if the vectors are numeric equals
	 */
	protected static boolean numericEquals(Vector<Object> vector1, Vector<Object> vector2) {
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
	 * Verifies if the new value is different from the field's old value. It's useful, for example, in NoSQL databases that
	 * replicates data between servers. This verification prevents to mark a field as modified and to be replicated needlessly. 
	 * @param field the field that we are checking before to be modified
	 * @param value the new value
	 * @return true if the new and the old values are different and the value was changed. 
	 */
	protected boolean setFieldIfNecessary(String field, Object value) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		if (!compareFieldValue(field, value)) {
			_doc.setField(field, value);
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean compareFieldValue(String field, Object value) {

		boolean equal = false;

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

		return equal;
	}

	@Override
	public T setField(String field, Object value) {
		isModified = setFieldIfNecessary(field, value) || isModified;
		return getThis();
	}

	@Override
	public Map<String, Vector<Object>> getFields() {
		Map<String, Vector<Object>> result = _doc.getFields();
		return result;
	}

	@Override
	public Vector<Object> getField(String field) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		Vector<Object> value = _doc.getField(field);
		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		String result = _doc.getFieldAsString(field);
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		int result = _doc.getFieldAsInteger(field);
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		double result = _doc.getFieldAsDouble(field);
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		Date result = _doc.getFieldAsDate(field);
		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		return _doc.isFieldEmpty(field);
	}

	@Override
	public boolean isModified() {
		return isModified;
	}

	@Override
	public boolean isOpen() {
		return _doc != null && _doc.isOpen();
	}

	@Override
	public boolean isNew() {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		boolean result = _doc.isNew();
		return result;
	}

	// @Override
	// public boolean isDeleted() {
	// return !getFieldAsString(FIELD_DELETED_AT).equals("");
	// }
	//
	// @Override
	// public org.riverframework.Document markDeleted() {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	// setField(FIELD_DELETED_AT, sdf.format(new Date()));
	// return getThis();
	// }
	//
	// @Override
	// public org.riverframework.Document unmarkDeleted() {
	// setField(FIELD_DELETED_AT, "");
	// return getThis();
	// }
	//

	@Override
	public T delete() {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		_doc.delete();
		return getThis();
	}

	@Override
	public T save(boolean force) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		if (force || isModified) {
			_doc.save();
			isModified = false;
		}

		return getThis();
	}

	@Override
	public T save() {
		save(true);
		return getThis();
	}

	@Override
	public boolean hasField(String field) {
		if (!isOpen()) throw new ClosedObjectException("The Document object is closed.");

		boolean result = _doc.hasField(field);
		return result;
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
		return ToStringBuilder.reflectionToString(this);
	}
}
