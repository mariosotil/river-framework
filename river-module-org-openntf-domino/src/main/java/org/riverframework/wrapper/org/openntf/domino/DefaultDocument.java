package org.riverframework.wrapper.org.openntf.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.DateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
	protected org.openntf.domino.Document _doc = null;

	public DefaultDocument(org.openntf.domino.Document d) {
		_doc = d;
	}

	@Override
	public org.openntf.domino.Document getNativeObject() {
		return _doc;
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
				// Always save as org.openntf.domino.DateTime
				org.openntf.domino.Session _session;
				org.openntf.domino.DateTime _date;
				_session = _doc.getParentDatabase().getParent();
				_date = _session.createDateTime((java.util.Date) temp.get(i));
				temp.set(i, _date);
			}
		}

		_doc.replaceItemValue(field, temp);

		return this;
	}

	@Override
	public String getObjectId() {
		String result = _doc.getUniversalID();
		return result;
	}

	@Override
	public Document recalc() {
		_doc.computeWithForm(true, false);
		return this;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Field getField(String field) {
		Field value = null;

		java.util.Vector temp = _doc.getItemValue(field);
		value = temp == null ? new DefaultField() : new DefaultField(temp);

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
		Vector<?> value = _doc.getItemValue(field);
		String result = value.size() > 0 ? Converter.getAsString(value.get(0)) : "";
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		Vector<?> value = _doc.getItemValue(field);
		Integer result = value.size() > 0 ? Converter.getAsInteger(value.get(0)) : 0;
		return result;
	}

	@Override
	public long getFieldAsLong(String field) {
		Vector<?> value = _doc.getItemValue(field);
		Long result = value.size() > 0 ? Converter.getAsLong(value.get(0)) : 0;
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		Vector<?> value = _doc.getItemValue(field);
		Double result = value.size() > 0 ? Converter.getAsDouble(value.get(0)) : 0;
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Vector<?> value = _doc.getItemValue(field);
		Object temp = value.size() > 0 ? value.get(0) : null;  
		if (temp != null && temp.getClass().getName().endsWith("DateTime")) {
			temp = ((DateTime) temp).toJavaDate();
		}
		
		Date result = Converter.getAsDate(temp);

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
	public boolean hasField(String field) {
		boolean result;
		result = _doc.hasItem(field);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Field> getFields() {
		Vector<org.openntf.domino.Item> items;
		items = _doc.getItems();

		Map<String, Field> result =
				new HashMap<String, Field>(items.size());

		for (org.openntf.domino.Item it : items) {
			String name = it.getName();
			java.util.Vector temp = it.getValues();
			Field values = temp == null ? new DefaultField() : new DefaultField(temp);

			if (values.isEmpty()) {
				values.add("");
			}

			if (values.get(0) instanceof org.openntf.domino.DateTime) {
				for (int i = 0; i < values.size(); i++) {
					// Always save as org.openntf.domino.DateTime
					Date _date = ((org.openntf.domino.DateTime) values.get(i)).toJavaDate();
					values.set(i, _date);
				}
			}

			result.put(name, values);
		}

		return result;
	}

	@Override
	public boolean isOpen() {
		return _doc != null;
	}

	@Override
	public boolean isNew() {
		boolean result;
		result = _doc.isNewNote();
		return result;
	}

	@Override
	public Document delete() {
		if (_doc != null) {
			_doc.removePermanently(true);
			_doc = null;
		}

		return this;
	}

	@Override
	public Document save() {
		_doc.save(true, false);
		return this;
	}

	@Override
	public void close() {
		_doc = null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}