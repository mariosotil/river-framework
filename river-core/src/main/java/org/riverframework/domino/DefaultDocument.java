package org.riverframework.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * Loads an IBM Notes document
 * <p>
 * This is a javadoc test
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */
public class DefaultDocument implements org.riverframework.domino.Document {
	public static final String FIELD_IS_CONFLICT = "$Conflict";
	public static final String FIELD_FORM = "Form";

	protected Database database = null;
	protected org.openntf.domino.Document _doc = null;
	protected boolean isModified = false;

	protected DefaultDocument(Database d, org.openntf.domino.Document doc) {
		database = d;
		_doc = doc;
		isModified = false;
	}

	@Override
	public org.riverframework.domino.Database getDatabase() {
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
			replaceFieldValue(field, value);
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

	@Override
	public String getUniversalId() {
		String result = _doc.getUniversalID();
		return result;
	}

	protected Document internalRecalc() {
		_doc.computeWithForm(true, false);
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean replaceFieldValue(String field, Object value) {
		java.util.Vector temp = null;

		if (value instanceof java.util.Vector) {
			temp = (Vector) ((java.util.Vector) value).clone();
		} else if (value instanceof java.util.Collection) {
			temp = new Vector((java.util.Collection) value);
		} else if (value instanceof String[]) {
			temp = new Vector(Arrays.asList((Object[]) value));
		} else {
			temp = new Vector(1);
			temp.add(value);
		}

		if (temp.get(0) instanceof java.util.Date) {
			Session session = DefaultSession.getInstance();

			for (int i = 0; i < temp.size(); i++) {
				// Always save as org.openntf.domino.DateTime
				temp.set(i, session.Java2NotesTime((java.util.Date) temp.get(i)));
			}
		}
		_doc.replaceItemValue(field, temp);

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getField(String field) {
		Vector<Object> value = null;

		value = _doc.getItemValue(field);

		// Always returns a java.util.Date object
		if (!value.isEmpty()) {
			if (value.get(0) instanceof org.openntf.domino.DateTime) {
				for (int i = 0; i < value.size(); i++) {
					value.set(i, ((org.openntf.domino.DateTime) value.get(i)).toJavaDate());
				}
			}
		}

		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		String result = _doc.getItemValueString(field);
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		int result = _doc.getItemValueInteger(field);
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		double result = _doc.getItemValueDouble(field);
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Date result = ((org.openntf.domino.DateTime) _doc
				.getItemValueDateTimeArray(field)
				.elementAt(0))
				.toJavaDate();

		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		if (!_doc.hasItem(field))
			return true;

		org.openntf.domino.Item item = _doc.getFirstItem(field);
		if (item != null) {
			if (item.getType() == org.openntf.domino.Item.RICHTEXT) {
				if (!_doc.getEmbeddedObjects().isEmpty()) {
					for (@SuppressWarnings("unchecked")
					Iterator<org.openntf.domino.EmbeddedObject> i = _doc.getEmbeddedObjects()
							.iterator(); i.hasNext();) {
						org.openntf.domino.EmbeddedObject eo = i.next();
						if (eo.getType() != 0)
							return false;
					}
				}
			}

			if (item.getText() != "")
				return false;
		}

		return true;
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
		return _doc != null;
	}

	@Override
	public boolean isNew() {
		boolean result = _doc.isNewNote();
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
	public org.riverframework.domino.DefaultDocument remove() {
		if (_doc != null) {
			_doc.remove(true);
			_doc = null;
		}

		return this;
	}

	@Override
	public Document save(boolean force) {
		if (force || isModified) {
			_doc.save(true, false);
			isModified = false;
		}

		return this;
	}

	@Override
	public Document save() {
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
		boolean result = _doc.hasItem(org.riverframework.domino.DefaultDocument.FIELD_IS_CONFLICT);
		return result;
	}

	protected Document afterCreate() {
		return this;
	}

	@Override
	public Document recalc() {
		return internalRecalc();
	}
}
