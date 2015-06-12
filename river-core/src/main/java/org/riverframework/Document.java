package org.riverframework;

import java.util.Map;

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
	 * Returns the object that wraps the native object. For example, if the wrapper loaded is
	 * River.LOTUS_DOMINO, and the object is an instance of org.riverframework.core.DefaultDocument,
	 * getNativeObject() will return an object that implements the org.riverframework.wrapper.Document interface.
	 * 
	 * @return the object used to wrap the native object
	 */
	@Override
	public org.riverframework.wrapper.Document<?> getWrapperObject();
	
	/**
	 * Compares a field from the Document with some value. If they are equal, it returns true
	 * 
	 * @param field
	 *            the field that value will be compared
	 * @param value
	 *            the value to be compared with the field
	 * @return true if they are equal
	 */
	public boolean compareFieldValue(String field, Object value);

	/**
	 * Set a field with the indicated value. If the field does not exist, it will create it.
	 * 
	 * @param field
	 *            the field to be set
	 * @param value
	 *            the new value
	 * @return the same Document, for method chaining
	 */
	public Document setField(String field, Object value);

	/**
	 * Returns the values of a field as a Vector object. If the field does not exist or is empty, it will returns an
	 * empty Vector.
	 * 
	 * @param field
	 *            the field to be get
	 * @return the values as a vector
	 */
	public Field getField(String field);

	/**
	 * Returns the value of a field as a String object. If the field is an array, it will returns the first value. If
	 * the field does not exist or is empty, it will returns an empty string.
	 * 
	 * @param field
	 *            the field to be get
	 * @return the value as a String
	 */
	public String getFieldAsString(String field);

	/**
	 * Returns the value of a field as an integer. If the field is an array, it will returns the first value. If the
	 * field does not exist or is empty, it will returns 0.
	 * 
	 * @param field
	 *            the field to be get
	 * @return the value as an integer.
	 */
	public int getFieldAsInteger(String field);

	/**
	 * Returns the value of a field as a long. If the field is an array, it will returns the first value. If the field
	 * does not exist or is empty, it will returns 0.
	 * 
	 * @param field
	 *            the field to be get
	 * @return the value as a long.
	 */
	public long getFieldAsLong(String field);

	/**
	 * Returns the value of a field as a double. If the field is an array, it will returns the first value. If the field
	 * does not exist or is empty, it will returns 0.
	 * 
	 * @param field
	 *            the field to be get
	 * @return the value as a double.
	 */
	public double getFieldAsDouble(String field);

	/**
	 * Returns the value of a field as a date. If the field is an array, it will returns the first value. If the field
	 * does not exist or is empty, it will returns null.
	 * 
	 * @param field
	 *            the field to be get
	 * @return the value as a java.util.Date.
	 */
	public java.util.Date getFieldAsDate(String field);

	/**
	 * Returns true if the field does not exists or is an empty string, zero or null.
	 * 
	 * @param field
	 *            the field to be evaluated
	 * @return true if the field is empty
	 */
	public boolean isFieldEmpty(String field);

	/**
	 * Returns true is any field of this document was changed since the last save.
	 * 
	 * @return true if the Document was modified
	 */
	public boolean isModified();

	/**
	 * Returns false if the wrapper Document is null or can't be opened.
	 * 
	 * @return true if the wrapper Database is opened
	 */
	public boolean isOpen();

	/**
	 * Returns true if the Document was recently created and still has not been saved.
	 * 
	 * @return true if is a new document.
	 */
	public boolean isNew();

	/**
	 * Makes a hard deletion. Its behavior will depend on how the wrapper loaded is implemented.
	 * 
	 * @return the same Document, for method chaining
	 */
	public Document delete();

	/**
	 * Saves the Document asking if it has to force it or do it only if the document was modified
	 * 
	 * @param force
	 *            how to save
	 * @return the same Document, for method chaining
	 */
	public Document save(boolean force);

	/**
	 * Saves the Document. Its behavior will depend on how the wrapper loaded is implemented.
	 * 
	 * @return the same Document, for method chaining
	 */
	public Document save();

	/**
	 * Requests a Document recalculation. Its behavior will depend on how the wrapper loaded is
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
	 * Returns all the fields and its a values as a HashMap object.
	 * 
	 * @return the fields and their values.
	 */
	public Map<String, Field> getFields();

}
