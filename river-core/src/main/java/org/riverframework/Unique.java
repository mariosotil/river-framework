package org.riverframework;
/**
 * Exposes the methods to make a document indexable.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Unique extends Document {
	/**
	 * Returns the Index that will be used to search documents of this class. 
	 * @return the index name
	 */
	public String getIndexName();

	/**
	 * Generate a new Id for the document each time is called.
	 * 
	 * @return the same Document, for method chaining
	 */
	public Document generateId();

	/**
	 * Set a new Id if we don't want to use generateId.
	 * 
	 * @param id
	 *            the new Id
	 * @return the same Document, for method chaining
	 */
	public Document setId(String id);

	/**
	 * Returns the current Document's Id, whether it was generated or set manually.
	 * @return the current Id
	 */
	public String getId();

}
