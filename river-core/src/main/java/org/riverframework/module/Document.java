package org.riverframework.module;

import java.util.Date;
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

public interface Document extends Base {
	public Document setField(String field, Object value);

	public String getObjectId();

	public Document recalc();

	public Vector<Object> getField(String field);

	public String getFieldAsString(String field);

	public int getFieldAsInteger(String field);

	public double getFieldAsDouble(String field);

	public Date getFieldAsDate(String field);

	public boolean isFieldEmpty(String field);

	public boolean hasField(String field);

	public Map<String, Vector<Object>> getFields();

	public boolean isOpen();

	public boolean isNew();

	public Document delete();

	public Document save();
}
