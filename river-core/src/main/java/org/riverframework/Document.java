package org.riverframework;

import java.util.Map;
import java.util.Vector;

/**
 * Loads an document
 * <p>
 * This is a javadoc test
 * 
 * @author mario.sotil@gmail.com
 * @version 0.0.x
 */

public interface Document {
	public static final String FIELD_ID = Session.FIELD_PREFIX + "id";
	public static final boolean FORCE_SAVE = true;

	public Database getDatabase();

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

	public Document delete();

	public Document save();

	public Document recalc();

	public String getForm();

	public String getUniversalId();

	public Document setForm(String form);

	public Map<String, Vector<Object>> getFields();

}
