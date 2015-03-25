package org.riverframework.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.riverframework.RiverException;

import lotus.domino.NotesException;

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
	protected lotus.domino.Document _doc = null;
	protected boolean isModified = false;

	protected DefaultDocument(Database d, lotus.domino.Document doc) {
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
		String result;
		try {
			result = _doc.getUniversalID();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	protected Document internalRecalc() {
		try {
			_doc.computeWithForm(true, false);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
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
				// Always save as lotus.domino.DateTime
				temp.set(i, session.Java2NotesTime((java.util.Date) temp.get(i)));
			}
		}

		try {
			_doc.replaceItemValue(field, temp);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Vector<Object>> getFields() {
		Vector<lotus.domino.Item> items;
		try {
			items = _doc.getItems();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Map<String, Vector<Object>> result = 
				new HashMap<String, Vector<Object>>(items.size());

		try {
			for(lotus.domino.Item it : items) {
				result.put(it.getName(),new Vector<Object>(it.getValues()));
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}


		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getField(String field) {
		Vector<Object> value = null;

		try {
			value = (Vector<Object>) _doc.getItemValue(field);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		// Always returns a java.util.Date object
		if (!value.isEmpty()) {
			if (value.get(0) instanceof lotus.domino.DateTime) {
				for (int i = 0; i < value.size(); i++) {
					try {
						value.set(i, ((lotus.domino.DateTime) value.get(i)).toJavaDate());
					} catch (NotesException e) {
						throw new RiverException(e);
					}
				}
			}
		}

		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		String result;
		try {
			result = _doc.getItemValueString(field);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		int result;
		try {
			result = _doc.getItemValueInteger(field);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		double result;
		try {
			result = _doc.getItemValueDouble(field);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Date result;
		try {
			result = ((lotus.domino.DateTime) _doc
					.getItemValueDateTimeArray(field)
					.elementAt(0))
					.toJavaDate();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		try {
			if (!_doc.hasItem(field))
				return true;

			lotus.domino.Item item = _doc.getFirstItem(field);
			if (item != null) {
				if (item.getType() == lotus.domino.Item.RICHTEXT) {
					if (!_doc.getEmbeddedObjects().isEmpty()) {
						for (@SuppressWarnings("unchecked")
						Iterator<lotus.domino.EmbeddedObject> i = _doc.getEmbeddedObjects()
						.iterator(); i.hasNext();) {
							lotus.domino.EmbeddedObject eo = i.next();
							if (eo.getType() != 0)
								return false;
						}
					}
				}

				if (item.getText() != "")
					return false;
			}
		} catch (NotesException e) {
			throw new RiverException(e);
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
		boolean result;
		try {
			result = _doc.isNewNote();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
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
	public org.riverframework.domino.Document delete() {
		if (_doc != null) {
			try {
				_doc.remove(true);
			} catch (NotesException e) {
				throw new RiverException(e);
			}
			_doc = null;
		}

		return this;
	}

	@Override
	public org.riverframework.domino.Document save(boolean force) {
		if (force || isModified) {
			try {
				_doc.save(true, false);
			} catch (NotesException e) {
				throw new RiverException(e);
			}
			isModified = false;
		}

		return this;
	}

	@Override
	public org.riverframework.domino.Document save() {
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
		boolean result;
		try {
			result = _doc.hasItem(org.riverframework.domino.DefaultDocument.FIELD_IS_CONFLICT);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
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
