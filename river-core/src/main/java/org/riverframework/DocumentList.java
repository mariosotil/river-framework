package org.riverframework;

import java.util.List;

import org.riverframework.DocumentIterator;

//TODO: evaluate if this class can be simplified with the View class
/**
 * Exposes the methods for control a collection of documents from a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface DocumentList extends List<org.riverframework.Document>, Iterable<org.riverframework.Document> {
	/**
	 * Returns the database where the collection's documents belong  
	 * @return a Database object
	 */
	public Database getDatabase();

	/**
	 * Loops around all the documents and call the delete method from each one. It would make a hard
	 * deletion. Anyway, its behavior will depend on how the wrapper loaded is implemented.
	 * 
	 * @return the same DocumentList, for method chaining
	 */
	public DocumentList deleteAll();
	
	/**
	 * Returns an iterator that delivers Document objects.
	 * 
	 * @return an iterator
	 */
	public DocumentIterator iterator();
}
