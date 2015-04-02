package org.riverframework;

import java.util.Map;
import java.util.Vector;

/**
 * This interface exposes the methods for control a document from a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Document extends Base {
	/**
	 * @return the Database where the Document is allocated
	 */
	public Database getDatabase();

	/**
	 * This method compares a field from the Document with some value. If they are equal, it returns true
	 * 
	 * @param field
	 *            the field that value will be compared
	 * @param value
	 *            the value to be compared with the field
	 * @return true if they are equal
	 */
	public boolean compareFieldValue(String field, Object value);

	/**
	 * This method set the field's value
	 * 
	 * @param field
	 *            the field to be set
	 * @param value
	 *            the new value
	 * @return the same Document, for method chaining
	 */
	public Document setField(String field, Object value);

	/**
	 * @param field
	 *            the field to be get
	 * @return the values as a vector
	 */
	public Vector<Object> getField(String field);

	/**
	 * @param field
	 *            the field to be get
	 * @return the value as a String
	 */
	public String getFieldAsString(String field);

	/**
	 * @param field
	 *            the field to be get
	 * @return the value as an integer. If the value can't be converted to an integer, it would returns zero. Anyway, it
	 *         will depend on how the module loaded is implemented.
	 */
	public int getFieldAsInteger(String field);

	/**
	 * @param field
	 *            the field to be get
	 * @return the value as a double. If the value can't be converted to a double, it would returns zero. Anyway, it
	 *         will depend on how the module loaded is implemented.
	 */
	public double getFieldAsDouble(String field);

	/**
	 * @param field
	 *            the field to be get
	 * @return the value as a java.util.Date. If the value can't be converted to a date, it would returns null. Anyway,
	 *         it will depend on how the module loaded is implemented.
	 */
	public java.util.Date getFieldAsDate(String field);

	/**
	 * @param field
	 *            the field to be evaluated
	 * @return true if the field's value is an empty String, zero or null
	 */
	public boolean isFieldEmpty(String field);

	/**
	 * @return true if any Document's field was modified
	 */
	public boolean isModified();

	/**
	 * This method set the modified flag. It SHOULD NOT be used at less you know what are you doing.
	 * 
	 * @param m
	 *            true to set the document as modified.
	 * @return the same Document, for method chaining
	 */
	// TODO: find the way to remove this method from the interface
	public Document setModified(boolean m);

	/**
	 * If the core Document object is created, but the module Document is null or can't be opened, this method will
	 * return false.
	 * 
	 * @return true if the module Database is opened
	 */
	public boolean isOpen();

	/**
	 * @return true if the Document was created and not saved. Its behavior will depend on how the module loaded is
	 *         implemented.
	 */
	public boolean isNew();

	/**
	 * This method would make a hard deletion. Its behavior will depend on how the module loaded is implemented.
	 * 
	 * @return the same Document, for method chaining
	 */
	public Document delete();

	/**
	 * This method would save the Document. Its behavior will depend on how the module loaded is implemented.
	 * 
	 * @return the same Document, for method chaining
	 */
	public Document save();

	/**
	 * This method would request a Document recalculation. Its behavior will depend on how the module loaded is
	 * implemented.
	 * 
	 * @return the same Document, for method chaining
	 */
	public Document recalc();

	/**
	 * @param field
	 *            the field to be evaluated
	 * @return true if the fields exists in the Document
	 */
	public boolean hasField(String field);

	/**
	 * @return all the Document's fields and their values as a HashMap object.
	 */
	public Map<String, Vector<Object>> getFields();

}
