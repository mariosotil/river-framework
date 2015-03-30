package org.riverframework.wrapper.org_openntf.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.riverframework.wrapper.Document;

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

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getField(String field) {
		Vector<Object> value = null;

		value = _doc.getItemValue(field);

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
	public boolean existField(String field) {
		boolean result;
		result = _doc.hasItem(field);
		return result;
	}

	@Override
	public Map<String, Vector<Object>> getFields() {
		Vector<org.openntf.domino.Item> items;
		items = _doc.getItems();

		Map<String, Vector<Object>> result =
				new HashMap<String, Vector<Object>>(items.size());

		for (org.openntf.domino.Item it : items) {
			result.put(it.getName(), new Vector<Object>(it.getValues()));
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
			_doc.remove(true);
			_doc = null;
		}

		return this;
	}

	@Override
	public Document save() {
		_doc.save(true, false);
		return this;
	}
}
