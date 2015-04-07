package org.riverframework.module.hazelcast;

import java.util.Date;
import java.util.Map;
import java.util.Vector;

import org.riverframework.module.Document;

/**
 * Loads an IBM Notes document
 * <p>
 * This is a javadoc test
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */
class DefaultDocument implements org.riverframework.module.Document {
	public DefaultDocument(Object d) {
	}

	@Override
	public Object getReferencedObject() {
		return null;
	}

	@Override
	public Document setField(String field, Object value) {
		return this;
	}

	@Override
	public String getObjectId() {
		return "";
	}

	@Override
	public Document recalc() {
		return this;
	}

	@Override
	public Vector<Object> getField(String field) {
		return null;
	}

	@Override
	public String getFieldAsString(String field) {
		return null;
	}

	@Override
	public int getFieldAsInteger(String field) {
		return 0;
	}

	@Override
	public double getFieldAsDouble(String field) {
		return 0;
	}

	@Override
	public Date getFieldAsDate(String field) {
		return null;
	}

	@Override
	public boolean isFieldEmpty(String field) {
		return true;
	}

	@Override
	public boolean hasField(String field) {
		return false;
	}

	@Override
	public Map<String, Vector<Object>> getFields() {
		return null;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public boolean isNew() {
		return true;
	}

	@Override
	public Document delete() {
		return this;
	}

	@Override
	public Document save() {
		return this;
	}

	@Override
	public void close() {
	}
}
