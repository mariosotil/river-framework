package org.riverframework.core;

/**
 * Exposes the methods to make a document indexable.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface IndexedDocument<T> extends Document {
	public String getIdField();

	public String getIndexName();

	/**
	 * Creates the Index that will be used to search documents of this class.
	 * it.
	 * 
	 * @return a DefaultView object with the index. The object will be closed if
	 *         it is not possible to create it.
	 */
	public View createIndex();

	/**
	 * Returns the Index that will be used to search documents of this class.
	 * it.
	 * 
	 * @return a DefaultView object with the index. The object will be closed if
	 *         it is not possible to find it.
	 */
	public View getIndex();

	/**
	 * Generate a new Id for the document each time is called.
	 * 
	 * @return the same Document, for method chaining
	 */
	public T generateId();

	/**
	 * Set a new Id if we don't want to use generateId.
	 * 
	 * @param id
	 *            the new Id
	 * @return the same Document, for method chaining
	 */
	public T setId(String id);

	/**
	 * Returns the current Document's Id, whether it was generated or set
	 * manually.
	 * 
	 * @return the current Id
	 */
	public String getId();

}
