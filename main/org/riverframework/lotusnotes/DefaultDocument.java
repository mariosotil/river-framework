package org.riverframework.lotusnotes;

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
public class DefaultDocument extends org.riverframework.fw.AbstractDocument<lotus.domino.Document> {
	public static final String FIELD_IS_CONFLICT = "$Conflict";
	public static final String FIELD_FORM = "Form";

	public DefaultDocument(DefaultDatabase d) {
		super(d);
	}

	public DefaultDocument(DefaultDatabase d, lotus.domino.Document doc) {
		super(d, doc);
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

	@Override
	protected org.riverframework.lotusnotes.DefaultDocument internalRecalc() {
		try {
			document.computeWithForm(true, false);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
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
				lotus.domino.Session session = DefaultSession.getInstance().getNotesSession();

				for (int i = 0; i < temp.size(); i++) {
					// Always save as lotus.domino.DateTime
					temp.set(i, session.createDateTime((java.util.Date) temp.get(i)));
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
				if (value.get(0) instanceof lotus.domino.DateTime) {
					for (int i = 0; i < value.size(); i++) {
						value.set(i, ((lotus.domino.DateTime) value.get(i)).toJavaDate());
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
			result = ((lotus.domino.DateTime) document.getItemValueDateTimeArray(field).elementAt(0))
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

			lotus.domino.Item item = document.getFirstItem(field);
			if (item != null) {
				if (item.getType() == lotus.domino.Item.RICHTEXT) {
					if (!document.getEmbeddedObjects().isEmpty()) {
						for (@SuppressWarnings("unchecked")
						Iterator<lotus.domino.EmbeddedObject> i = document.getEmbeddedObjects()
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
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return true;
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
	public org.riverframework.lotusnotes.DefaultDocument remove() {
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

	@Override
	protected org.riverframework.lotusnotes.DefaultDocument saveImplementation() {
		try {
			document.save(true, false);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public DefaultDocument save(boolean force) {
		super.save(force);
		return this;
	}

	@Override
	public DefaultDocument save() {
		super.save();
		return this;
	}

	@Override
	protected void close() {
		try {
			if (document != null) {
				document.recycle();
				document = null;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	public String getForm() {
		return getFieldAsString(FIELD_FORM);
	}

	public org.riverframework.lotusnotes.DefaultDocument setForm(String form) {
		setField(FIELD_FORM, form);
		return this;
	}

	public boolean isConflict() {
		boolean result;
		try {
			result = document.hasItem(org.riverframework.lotusnotes.DefaultDocument.FIELD_IS_CONFLICT);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	protected org.riverframework.lotusnotes.DefaultDocument afterCreate() {
		return this;
	}

}
