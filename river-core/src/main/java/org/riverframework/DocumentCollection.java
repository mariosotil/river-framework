package org.riverframework;

import java.util.List;

//TODO: evaluate if this class can be simplified with the View class
/**
 * Exposes the methods for control a collection of documents from a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface DocumentCollection extends List<org.riverframework.Document> {
	/**
	 * Returns the database where the documents that are in the collection belong  
	 * @return a Database object
	 */
	public Database getDatabase();

	/**
	 * Loops around all the documents and call the delete method from each one. It would make a hard
	 * deletion. Anyway, its behavior will depend on how the module loaded is implemented.
	 * 
	 * @return the same DocumentCollection, for method chaining
	 */
	public DocumentCollection deleteAll();
}
