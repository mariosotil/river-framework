package org.riverframework.core;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.riverframework.Base;
import org.riverframework.Database;
import org.riverframework.Document;

/**
 * Loads an IBM Notes document
 * <p>
 * This is a javadoc test
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */
public class DefaultDocument implements org.riverframework.Document {
	public static final String FIELD_IS_CONFLICT = "$Conflict";
	public static final String FIELD_FORM = "Form";

	protected Database database = null;
	protected org.riverframework.wrapper.Document _doc = null;
	protected boolean isModified = false;

	protected DefaultDocument(Database d, org.riverframework.wrapper.Document _d) {
		database = d;
		_doc = _d;
		isModified = false;
	}

	@Override
	public String getObjectId() {
		String result = _doc.getObjectId();
		return result;
	}

	@Override
	public Base getParent() {
		return database;
	}

	@Override
	public Object getWrappedObject() {
		return _doc;
	}

	@Override
	public org.riverframework.Database getDatabase() {
		return database;
	}

	public static String getIndexName() {
		return "";
	}

	protected static boolean numericEquals(Vector<Object> c1, Vector<Object> c2) {
		if (c1.size() != c2.size())
			return false;
		if (c1.isEmpty())
			return true;

		Iterator<Object> it1 = c1.iterator();
		Iterator<Object> it2 = c2.iterator();

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

	protected boolean setFieldIfNecessary(String field, Object value) {
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
	public Document setField(String field, Object value) {
		isModified = setFieldIfNecessary(field, value) || isModified;
		return this;
	}

	protected Document internalRecalc() {
		_doc.recalc();
		return this;
	}

	@Override
	public Map<String, Vector<Object>> getFields() {
		Map<String, Vector<Object>> result = _doc.getFields();
		return result;
	}

	@Override
	public Vector<Object> getField(String field) {
		Vector<Object> value = _doc.getField(field);
		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		String result = _doc.getFieldAsString(field);
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		int result = _doc.getFieldAsInteger(field);
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		double result = _doc.getFieldAsDouble(field);
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Date result = _doc.getFieldAsDate(field);
		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		return _doc.isFieldEmpty(field);
	}

	@Override
	public boolean isModified() {
		return isModified;
	}

	@Override
	public Document setModified(boolean m) {
		isModified = m;
		return this;
	}

	@Override
	public boolean isOpen() {
		return _doc != null && _doc.isOpen();
	}

	@Override
	public boolean isNew() {
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
	// return this;
	// }
	//
	// @Override
	// public org.riverframework.Document unmarkDeleted() {
	// setField(FIELD_DELETED_AT, "");
	// return this;
	// }
	//

	@Override
	public org.riverframework.Document delete() {
		_doc.delete();
		return this;
	}

	public org.riverframework.Document save(boolean force) {
		if (force || isModified) {
			_doc.save();
			isModified = false;
		}

		return this;
	}

	@Override
	public org.riverframework.Document save() {
		save(true);
		return this;
	}

	@Override
	public String getForm() {
		return getFieldAsString(FIELD_FORM);
	}

	@Override
	public Document setForm(String form) {
		setField(FIELD_FORM, form);
		return this;
	}

	public boolean isConflict() {
		boolean result = _doc.existField(org.riverframework.core.DefaultDocument.FIELD_IS_CONFLICT);
		return result;
	}

	protected Document afterCreate() {
		return this;
	}

	@Override
	public Document recalc() {
		return internalRecalc();
	}

	@Override
	public void close() {
		_doc.close();
	}
}
