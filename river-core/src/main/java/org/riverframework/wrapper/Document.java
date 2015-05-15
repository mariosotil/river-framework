package org.riverframework.wrapper;

import java.util.Date;
import java.util.Map;

import org.riverframework.Field;

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

	public Document recalc();

	public Field getField(String field);

	public String getFieldAsString(String field);

	public int getFieldAsInteger(String field);

	public long getFieldAsLong(String field);

	public double getFieldAsDouble(String field);

	public Date getFieldAsDate(String field);

	public boolean isFieldEmpty(String field);

	public boolean hasField(String field);

	public Map<String, Field> getFields();

	public boolean isNew();

	public Document delete();

	public Document save();
}
