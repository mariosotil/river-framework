package org.riverframework.wrapper.hazelcast;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
	public static final String FIELD_UUID = "RIVER_HAZELCAST_UUID";
	protected boolean newDocument = false;
	
	protected Map<String, Object> _doc = null;

	public DefaultDocument(Map<String, Object> d) {
		_doc = d;
	}

	@Override
	public Map<String, Object> getNativeObject() {
		// TODO: protect the key UUID
		return _doc;
	}

	@Override
	public Document setField(String field, final Object value) {
		if (value instanceof Object[]) {
			Field vector = new DefaultField(Arrays.asList((Object[]) value));
			_doc.put(field, vector);
		} else {
			_doc.put(field, value);
		}
		
		return this;
	}

	@Override
	public String getObjectId() {
		return _doc.get(FIELD_UUID).toString();
	}

	@Override
	public Document recalc() {
		// TODO: ??
		return this;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Field getField(String field) {
		Object value = _doc.get(field);
		Field vector = null;

		if (value instanceof Vector<?>) {
			vector = new DefaultField((java.util.Vector) value);
		} else {
			vector = new DefaultField();
			vector.add(value);
		}

		return vector;
	}

	@Override
	public String getFieldAsString(String field) {		
		Object value = _doc.get(field);
		Object temp = null;

		if (!(value instanceof Vector<?>)) {
			temp = value;			
		} else if (((Vector<?>) value).size() > 0) {		
			temp = ((Vector<?>) value).get(0);
		}

		String result = Converter.getAsString(temp);
		return result;
	}

	@Override
	public int getFieldAsInteger(String field) {
		Object value = _doc.get(field);
		Object temp = null;

		if (!(value instanceof Vector<?>)) {
			temp = value;			
		} else if (((Vector<?>) value).size() > 0) {		
			temp = ((Vector<?>) value).get(0);
		}

		Integer result = Converter.getAsInteger(temp);
		return result;
	}

	@Override
	public long getFieldAsLong(String field) {
		Object value = _doc.get(field);
		Object temp = null;

		if (!(value instanceof Vector<?>)) {
			temp = value;			
		} else if (((Vector<?>) value).size() > 0) {		
			temp = ((Vector<?>) value).get(0);
		}

		Long result = Converter.getAsLong(temp);
		return result;
	}

	@Override
	public double getFieldAsDouble(String field) {
		Object value = _doc.get(field);
		Object temp = null;

		if (!(value instanceof Vector<?>)) {
			temp = value;			
		} else if (((Vector<?>) value).size() > 0) {		
			temp = ((Vector<?>) value).get(0);
		}

		Double result = Converter.getAsDouble(temp);
		return result;
	}

	@Override
	public Date getFieldAsDate(String field) {
		Object value = _doc.get(field);
		Object temp = null;

		if (!(value instanceof Vector<?>)) {
			temp = value;			
		} else if (((Vector<?>) value).size() > 0) {		
			temp = ((Vector<?>) value).get(0);
		}

		Date result = Converter.getAsDate(temp);
		return result;	
	}

	@Override
	public boolean isFieldEmpty(String field) {
		return getFieldAsString(field).equals("");
	}

	@Override
	public boolean hasField(String field) {
		return _doc.containsKey(field);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Field> getFields() {
		Map<String, Field> result = new HashMap<String, Field>();
		
		for(String key : _doc.keySet()) {
			Object value = _doc.get(key);
			if (value instanceof Vector) {
				result.put(key, new DefaultField((java.util.Vector) value));
			} else {
				Field vector = new DefaultField();
				vector.add(value);
				result.put(key, vector);
			}
		}
		
		return result;
	}

	@Override
	public boolean isOpen() {
		return _doc != null;
	}

	@Override
	public boolean isNew() {
		return newDocument;
	}

	@Override
	public Document delete() {
		_doc = null;
		
		return this;
	}

	@Override
	public Document save() {
		// TODO: serialize?
		newDocument = false;
		
		return this;
	}

	@Override
	public void close() {
		_doc = null; 
	}
}
