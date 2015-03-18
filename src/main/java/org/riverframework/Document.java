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

public interface Document {
	public static final String FIELD_CLASS = Session.PREFIX + "Class";
	public static final String FIELD_ID = Session.PREFIX + "Id";
	public static final boolean FORCE_SAVE = true;

	public boolean compareFieldValue(String field, Object value);

	public Document setField(String field, Object value);

	public Vector<Object> getField(String field);

	public String getFieldAsString(String field);

	public int getFieldAsInteger(String field);

	public double getFieldAsDouble(String field);

	public java.util.Date getFieldAsDate(String field);

	public boolean isFieldEmpty(String field);

	public boolean isModified();

	public Document setModified(boolean m);

	public boolean isOpen();

	public boolean isNew();

	public Document remove();

	public Document save(boolean force);

	public Document save();

	public Document recalc();

	public Database getDatabase();

}
