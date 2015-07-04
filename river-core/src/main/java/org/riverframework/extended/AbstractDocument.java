package org.riverframework.extended;

import java.util.Date;
import java.util.Map;

import org.riverframework.core.Database;
import org.riverframework.core.Document;
import org.riverframework.core.Field;

public abstract class AbstractDocument<T extends AbstractDocument<T>> implements Base, Document {
	protected Document doc = null;
	
	public AbstractDocument(Document doc) {
		this.doc = doc;
	}	
	
	@Override
	public String getObjectId() {
		return doc.getObjectId();
	}
		
	@Override
	public boolean isOpen() {
		return (doc != null && doc.isOpen());
	}
	
	public org.riverframework.core.Document getDocument() {
		return doc;
	}
	
	public T save() {
		doc.save();
		
		return getThis();
	}
	
	protected abstract T getThis();
	
	@Override
	public void close() {
		doc.close();
	}

	@Override
	public Database getDatabase() {
		return doc.getDatabase();
	}

	@Override
	public org.riverframework.wrapper.Document<?> getWrapperObject() {
		return doc.getWrapperObject();
	}

	@Override
	public boolean compareFieldValue(String field, Object value) {
		return doc.compareFieldValue(field, value);
	}

	@Override
	public Document setField(String field, Object value) {
		return doc.setField(field, value);
	}

	@Override
	public Field getField(String field) {
		return doc.getField(field);
	}

	@Override
	public String getFieldAsString(String field) {
		return doc.getFieldAsString(field);
	}

	@Override
	public int getFieldAsInteger(String field) {
		return doc.getFieldAsInteger(field);
	}

	@Override
	public long getFieldAsLong(String field) {
		return doc.getFieldAsLong(field);
	}

	@Override
	public double getFieldAsDouble(String field) {
		return doc.getFieldAsDouble(field);
	}

	@Override
	public Date getFieldAsDate(String field) {
		return doc.getFieldAsDate(field);
	}

	@Override
	public boolean isFieldEmpty(String field) {
		return doc.isFieldEmpty(field);
	}

	@Override
	public boolean isModified() {
		return doc.isModified();
	}

	@Override
	public boolean isNew() {
		return doc.isNew();
	}

	@Override
	public Document delete() {
		return doc.delete();
	}

	@Override
	public Document save(boolean force) {
		return doc.save(force);
	}

	@Override
	public Document recalc() {
		return doc.recalc();
	}

	@Override
	public boolean hasField(String field) {
		return doc.hasField(field);
	}

	@Override
	public Map<String, Field> getFields() {
		return doc.getFields();
	}
}
