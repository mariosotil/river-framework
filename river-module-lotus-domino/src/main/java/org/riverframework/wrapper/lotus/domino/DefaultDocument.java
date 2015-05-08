package org.riverframework.wrapper.lotus.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Item;
import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.log4j.Logger;
import org.riverframework.RiverException;
import org.riverframework.core.DefaultField;
import org.riverframework.utils.Converter;
import org.riverframework.wrapper.Document;
import org.riverframework.Field;

/**
 * Loads an IBM Notes document
 * <p>
 * This is a javadoc test
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */
class DefaultDocument implements org.riverframework.wrapper.Document {
	// static Logger logWrapper = Logger.getLogger(DefaultDocument.class.getName());
	protected org.riverframework.wrapper.Session session = null;
	protected lotus.domino.Document __doc = null;

	public DefaultDocument(org.riverframework.wrapper.Session s, lotus.domino.Document d) {
		__doc = d;
		session = s;
		((DefaultSession) session).registerObject(__doc);
	}

	@Override
	public lotus.domino.Document getNativeObject() {
		return __doc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Document setField(String field, Object value) {
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
			for (int i = 0; i < temp.size(); i++) {
				// Always save as lotus.domino.DateTime
				lotus.domino.Session _session;
				lotus.domino.DateTime _date;
				try {
					_session = __doc.getParentDatabase().getParent();
					_date = _session.createDateTime((java.util.Date) temp.get(i));
				} catch (NotesException e) {
					throw new RiverException(e);
				}
				temp.set(i, _date);
			}
		}

		try {
			__doc.replaceItemValue(field, temp);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public String getObjectId() {
		String result;
		try {
			result = __doc.getUniversalID();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Document recalc() {
		try {
			__doc.computeWithForm(true, false);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Field getField(String field) {
		Field value = null;

		try {
			Vector<Object> temp = __doc.getItemValue(field);
			value = temp == null ? new DefaultField() : new DefaultField(temp);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

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
		String result = null;
		try {
			Vector<?> value = __doc.getItemValue(field);
			result = value.size() > 0 ? Converter.getAsString(value.get(0)) : "";
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		int result = 0;
		try {
			Vector<?> value = __doc.getItemValue(field);
			result = value.size() > 0 ? Converter.getAsInteger(value.get(0)) : 0;
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public long getFieldAsLong(String field) {
		long result = 0;
		try {
			Vector<?> value = __doc.getItemValue(field);
			result = value.size() > 0 ? Converter.getAsLong(value.get(0)) : 0;
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		double result;
		try {
			Vector<?> value = __doc.getItemValue(field);
			result = value.size() > 0 ? Converter.getAsDouble(value.get(0)) : 0;
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Date result;
		try {
			Vector<?> value = __doc.getItemValue(field);
			Object temp = value.size() > 0 ? value.get(0) : null;  
			if (temp != null && temp.getClass().getName().endsWith("DateTime")) {
				temp = ((DateTime) temp).toJavaDate();
			}
			
			result = Converter.getAsDate(temp);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		boolean result = true;

		try {
			if (__doc.hasItem(field)) {
				lotus.domino.Item _item = __doc.getFirstItem(field);
				if (_item != null) {
					if (_item.getType() == lotus.domino.Item.RICHTEXT) {
						if (!__doc.getEmbeddedObjects().isEmpty()) {
							for (@SuppressWarnings("unchecked")
							Iterator<lotus.domino.EmbeddedObject> i = __doc.getEmbeddedObjects()
									.iterator(); i.hasNext();) {
								lotus.domino.EmbeddedObject eo = i.next();
								if (eo.getType() != 0) {
									result = false;
									break;
								}
							}
						}
					}

					if (result && _item.getText() != "")
						result = false;

					try {
						_item.recycle();
					} catch (NotesException e) {
						throw new RiverException(e);
					}
				}
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean hasField(String field) {
		boolean result;
		try {
			result = __doc.hasItem(field);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Field> getFields() {
		Map<String, Field> result = null;

		try {
			// logWrapper.debug("getFields: " + _doc.getUniversalID());
			// logWrapper.debug("getFields: loading items");

			Vector<lotus.domino.Item> items;
			items = __doc.getItems();
			((DefaultSession) session).registerVector(items);

			// logWrapper.debug("getFields: found " + items.size());
			result = new HashMap<String, Field>(items.size());

			for (lotus.domino.Item it : items) {
				String name = it.getName();
				int type = it.getType();
				// logWrapper.debug("getFields: item=" + name + ", type=" + type);

				Field values = null;

				if (type == Item.DATETIMES
						|| type == Item.NAMES
						|| type == Item.NUMBERS
						|| type == Item.READERS
						|| type == Item.RICHTEXT
						|| type == Item.TEXT) {
					Vector<Object> temp = it.getValues();
					values = temp == null ? new DefaultField() : new DefaultField(temp);
				} else {
					values = new DefaultField();
				}

				if (values.isEmpty()) {
					// logWrapper.debug("getFields: it's empty");
					values.add("");
				}

				if (!values.isEmpty()) {
					if (values.get(0) instanceof lotus.domino.DateTime) {
						// logWrapper.debug("getFields: it's datetime");
						for (int i = 0; i < values.size(); i++) {
							values.set(i, ((lotus.domino.DateTime) values.get(i)).toJavaDate());
						}
					}
				}

				// logWrapper.debug("getFields: saving into the map");
				result.put(name, values);
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public boolean isOpen() {
		return __doc != null;
	}

	@Override
	public boolean isNew() {
		boolean result;
		try {
			result = __doc.isNewNote();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Document delete() {
		if (__doc != null) {
			try {
				__doc.removePermanently(true);
				__doc.recycle();
			} catch (NotesException e) {
				throw new RiverException(e);
			}
			__doc = null;
		}

		return this;
	}

	@Override
	public Document save() {
		try {
			__doc.save(true, false);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			if (__doc != null)
				__doc.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__doc = null;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
