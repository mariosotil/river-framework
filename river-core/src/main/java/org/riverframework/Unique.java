package org.riverframework;

public interface Unique extends Document {
	/**
	 * This method should generate a new Id for the document each time is called.
	 * 
	 * @return the same Document, for method chaining
	 */
	public Document generateId();

	/**
	 * This method should set a new Id if we dont want to se generateId.
	 * 
	 * @param id
	 *            the new Id
	 * @return the same Document, for method chaining
	 */
	public Document setId(String id);

	/**
	 * @return the current Document's Id
	 */
	public String getId();

}
