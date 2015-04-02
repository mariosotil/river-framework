package org.riverframework;

import java.util.List;

//TODO: evaluate if this class can be simplified with the View class
/**
 * This interface exposes the methods for control a collection of documents from a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface DocumentCollection extends List<org.riverframework.Document> {
	/**
	 * @return the Database the Documents into the DocumentCollection are allocated
	 */
	public Database getDatabase();

	/**
	 * This method receives a module DocumentCollection and loads itself with the core Documents for each module
	 * Document retrieved.
	 * 
	 * @param _col
	 *            the module DocumentCollection
	 * @return the same DocumentCollection, for method chaining
	 */
	public DocumentCollection loadFrom(org.riverframework.module.DocumentCollection _col);

	/**
	 * This method loops around all the documents and call the delete method from each one. It would make a hard
	 * deletion. Anyway, its behavior will depend on how the module loaded is implemented.
	 * 
	 * @return the same DocumentCollection, for method chaining
	 */
	public DocumentCollection deleteAll();
}
