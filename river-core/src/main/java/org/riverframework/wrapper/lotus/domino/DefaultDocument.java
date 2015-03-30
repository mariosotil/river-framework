package org.riverframework.wrapper.lotus.domino;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import lotus.domino.NotesException;

import org.riverframework.RiverException;
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
	protected lotus.domino.Document _doc = null;

	public DefaultDocument(lotus.domino.Document d) {
		_doc = d;
	}

	@Override
	public Object getWrappedObject() {
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
				// Always save as lotus.domino.DateTime
				lotus.domino.Session _session;
				lotus.domino.DateTime _date;
				try {
					_session = _doc.getParentDatabase().getParent();
					_date = _session.createDateTime((java.util.Date) temp.get(i));
				} catch (NotesException e) {
					throw new RiverException(e);
				}
				temp.set(i, _date);
			}
		}

		try {
			_doc.replaceItemValue(field, temp);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public String getObjectId() {
		String result;
		try {
			result = _doc.getUniversalID();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Document recalc() {
		try {
			_doc.computeWithForm(true, false);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getField(String field) {
		Vector<Object> value = null;

		try {
			value = _doc.getItemValue(field);
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
		String result;
		try {
			result = _doc.getItemValueString(field);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result == null ? "" : result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		int result = 0;
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
	public boolean existField(String field) {
		boolean result;
		try {
			result = _doc.hasItem(field);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
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
			for (lotus.domino.Item it : items) {
				result.put(it.getName(), new Vector<Object>(it.getValues()));
			}
		} catch (NotesException e) {
			throw new RiverException(e);
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
		try {
			result = _doc.isNewNote();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public Document delete() {
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
	public Document save() {
		try {
			_doc.save(true, false);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			if (_doc != null)
				_doc.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			_doc = null;
		}
	}
}
