package org.riverframework;

import java.util.Vector;

/*
 * Loads an document
 * <p>
 * This is a javadoc test
 * 
 *  @author mario.sotil@gmail.com
 *  @version 0.0.x
 */

public interface Document<T> {
	public static final String FIELD_CLASS = Session.PREFIX + "Class";
	public static final String FIELD_ID = Session.PREFIX + "Id";
	public static final boolean FORCE_SAVE = true;

	public org.riverframework.Document<T> generateId();

	public String getId();

	public String getUniversalId();

	public boolean compareFieldValue(String field, Object value);

	public org.riverframework.Document<T> setField(String field, Object value);

	public Vector<Object> getField(String field);

	public String getFieldAsString(String field);

	public int getFieldAsInteger(String field);

	public double getFieldAsDouble(String field);

	public java.util.Date getFieldAsDate(String field);

	public boolean isFieldEmpty(String field);

	public boolean isModified();

	public boolean isOpen();

	public boolean isNew();

	public org.riverframework.Document<T> remove();

	public org.riverframework.Document<T> save(boolean force);

	public org.riverframework.Document<T> save();

	public org.riverframework.Document<T> recalc();

	public org.riverframework.Database<?> getDatabase();
}
