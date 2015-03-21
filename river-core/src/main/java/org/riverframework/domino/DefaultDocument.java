package org.riverframework.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.riverframework.RiverException;

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

	protected static Database rDatabase = null;
	protected org.openntf.domino.Document document = null;
	protected boolean isModified = false;

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
			if (((Number) it1.next()).doubleValue() != ((Number) it2.next()).doubleValue())
				return false;
		}

		return true;
	}

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
	public Document setField(String field, Object value) {
		isModified = setFieldIfNecessary(field, value) || isModified;
		return this;
	}

	protected DefaultDocument(Database d, org.openntf.domino.Document doc) {
		rDatabase = d;
		document = doc;
		isModified = false;
	}

	@Override
	public String getUniversalId() {
		String result = "";
		try {
			result = document.getUniversalID();
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	protected Document internalRecalc() {
		try {
			document.computeWithForm(true, false);
		} catch (Exception e) {
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

		try {
			if (temp.get(0) instanceof java.util.Date) {
				Session session = DefaultSession.getInstance();

				for (int i = 0; i < temp.size(); i++) {
					// Always save as org.openntf.domino.DateTime
					temp.set(i, session.Java2NotesTime((java.util.Date) temp.get(i)));
				}
			}
			document.replaceItemValue(field, temp);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getField(String field) {
		Vector<Object> value = null;

		try {
			value = document.getItemValue(field);

			// Always returns a java.util.Date object
			if (!value.isEmpty()) {
				if (value.get(0) instanceof org.openntf.domino.DateTime) {
					for (int i = 0; i < value.size(); i++) {
						value.set(i, ((org.openntf.domino.DateTime) value.get(i)).toJavaDate());
					}
				}
			}
		} catch (Exception e) {
			throw new RiverException(e);
		} finally {
			if (value == null)
				value = new Vector<Object>();
		}

		return value;
	}

	@Override
	public String getFieldAsString(String field) {
		String result = "";

		try {
			if (document.hasItem(field)) {
				result = document.getItemValueString(field);
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		int result = 0;

		try {
			result = document.getItemValueInteger(field);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		double result = 0;

		try {
			result = document.getItemValueDouble(field);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Date result = null;
		try {
			result = ((org.openntf.domino.DateTime) document.getItemValueDateTimeArray(field).elementAt(0))
					.toJavaDate();
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		try {
			if (!document.hasItem(field))
				return true;

			org.openntf.domino.Item item = document.getFirstItem(field);
			if (item != null) {
				if (item.getType() == org.openntf.domino.Item.RICHTEXT) {
					if (!document.getEmbeddedObjects().isEmpty()) {
						for (@SuppressWarnings("unchecked")
						Iterator<org.openntf.domino.EmbeddedObject> i = document.getEmbeddedObjects()
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
		} catch (Exception e) {
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
		return document != null;
	}

	@Override
	public boolean isNew() {
		boolean result = false;

		try {
			result = document.isNewNote();
		} catch (Exception e) {
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
	public org.riverframework.domino.DefaultDocument remove() {
		try {
			if (document != null) {
				document.remove(true);
				document = null;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
		return this;
	}

	protected Document saveImplementation() {
		try {
			document.save(true, false);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public Document save(boolean force) {
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
		boolean result;
		try {
			result = document.hasItem(org.riverframework.domino.DefaultDocument.FIELD_IS_CONFLICT);
		} catch (Exception e) {
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

	@Override
	public Database getDatabase() {
		return rDatabase;
	}

}
