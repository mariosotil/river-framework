package org.riverframework.core;

/**
 * Exposes the methods for control a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Database extends Base {
	/**
	 * Returns the Session that instanced the database
	 * 
	 * @return a Session object.
	 */
	public Session getSession();

	/**
	 * Returns the object that wraps the native object. For example, if the
	 * wrapper loaded is River.LOTUS_DOMINO, and the object is an instance of
	 * org.riverframework.core.DefaultDocument, getNativeObject() will return an
	 * object that implements the org.riverframework.wrapper.Document interface.
	 * 
	 * @return the object used to wrap the native object
	 */
	@Override
	public org.riverframework.wrapper.Database<?> getWrapperObject();

	/**
	 * * Creates a document. The parameters needed will depend on what wrapper
	 * is loaded.
	 * 
	 * @param parameters
	 *            a set of Strings that will let the Database create a Document.
	 * @return a new Document
	 */
	public Document createDocument(String... parameters);

	/**
	 * Creates a DefaultView object. The parameters needed will depend on what
	 * wrapper is being used.
	 * 
	 * @param parameters
	 *            a set of Strings that will let the Database creates a new
	 *            View.
	 * @return a DefaultView object
	 */
	public View createView(String... parameters);

	/**
	 * Creates an instance from the clazz class. The parameters needed will
	 * depend in what wrapper is being used.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractView core class
	 * @param parameters
	 *            a set of Strings that will let the Database creates a new
	 *            View.
	 * @return a new instance from the clazz class.
	 */
	public <U extends AbstractView<?>> U createView(Class<U> clazz,
			String... parameters);

	/**
	 * Returns an existent view. The parameters needed will depend on what
	 * wrapper is being used.
	 * 
	 * @param parameters
	 *            a set of Strings that will let the Database get an existent
	 *            View.
	 * @return a DefaultView object
	 */
	public View getView(String... parameters);

	/**
	 * Returns an instance from the clazz class. The parameters needed will
	 * depend on what wrapper is being used.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractView core class
	 * @param parameters
	 *            a set of Strings that will let the Database creates a new
	 *            View.
	 * @return a new instance from the clazz class.
	 */
	public <U extends AbstractView<?>> U getView(Class<U> clazz,
			String... parameters);

	/**
	 * Returns a closed view, as a Null Object Pattern. Its method isOpen() will
	 * return false.
	 * 
	 * @return a closed view.
	 */
	public View getClosedView();

	/**
	 * Returns a closed instance from the clazz class, as a Null Object Pattern.
	 * The method isOpen() will return false.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractView core class
	 * @return a closed view.
	 */
	public <U extends AbstractView<?>> U getClosedView(Class<U> clazz);

	/**
	 * Returns an existent Document. The parameters needed will depend on what
	 * wrapper is being used.
	 * 
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent
	 *            new Document.
	 * @return the Document found. If no document is found, the method returns a
	 *         closed Document object
	 */
	public Document getDocument(String... parameters);

	/**
	 * Returns a document with the option to created it if can't be found.
	 * 
	 * @param createIfDoesNotExist
	 *            if true, the document will be created.
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent
	 *            new Document.
	 * @return a DefaultDocument instance.
	 */
	public Document getDocument(boolean createIfDoesNotExist,
			String... parameters);

	/**
	 * Returns a Document object. The class is detected from the object doc
	 * using the rules defined from the classes registered at the database,
	 * using registerDocumentClass.
	 * 
	 * @param _doc
	 *            a wrapper Document object
	 * @return a Document object using the doc provided as parameter.
	 */
	public Document getDocument(org.riverframework.wrapper.Document<?> _doc);

	/**
	 * Creates an instance from the clazz class.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractDocument core class
	 * @param parameters
	 *            a set of Strings that will let the Database creates a new
	 *            Document.
	 * @return a DefaultDocument instance.
	 */
	public <U extends AbstractDocument<?>> U createDocument(Class<U> clazz,
			String... parameters);

	/**
	 * Returns an instance from the clazz class.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractDocument core class
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent
	 *            Document.
	 * @return a DefaultDocument instance.
	 */
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz,
			String... parameters);

	/**
	 * Returns an instance from the clazz class, with the option to created it
	 * if can't be found.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractDocument core class
	 * @param createIfDoesNotExist
	 *            if true, the document will be created.
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent
	 *            Document.
	 * @return a DefaultDocument instance.
	 */
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz,
			boolean createIfDoesNotExist, String... parameters);

	/**
	 * Returns an instance from the clazz class.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractDocument core class
	 * @param _doc
	 *            a wrapper Document object
	 * @return a Document object using the doc provided as parameter.
	 */
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz,
			org.riverframework.wrapper.Document<?> _doc);

	/**
	 * Returns a closed document, as a Null Object Pattern. Its method isOpen()
	 * will return false.
	 * 
	 * @return a closed document.
	 */
	public Document getClosedDocument();

	/**
	 * Returns a closed instance from the clazz class, as a Null Object Pattern.
	 * The method isOpen() will return false.
	 * 
	 * @param clazz
	 *            a class that extends the AbstractDocument core class
	 * @return a closed document.
	 */
	public <U extends AbstractDocument<?>> U getClosedDocument(Class<U> clazz);

	/**
	 * Returns all documents from the database as a DocumentIterator object,
	 * that implements List
	 * 
	 * @return a DocumentIterator document
	 */
	public DocumentIterator getAllDocuments();

	/**
	 * Deletes the current database. The function isOpen will return false after
	 * call it.
	 */
	public void delete();

	/**
	 * Returns the documents that match the query as a DocumentIterator object.
	 * The style of the query depends on how is implemented in the wrapper
	 * loaded. For example, in IBM Notes is just something like "Black AND Dog"
	 * 
	 * @param query
	 *            the query written in the format expected by the wrapper.
	 * @return a DocumentIterator document
	 */
	public DocumentIterator search(String query);

	/**
	 * Refresh the database index. How this index is refreshed will depend on
	 * how is implemented in the wrapper loaded.
	 * 
	 * @param createIfDoesNotExist
	 *            if the index does not exist, set true to force to be created.
	 *            Otherwise, select false.
	 * @return the object itself for method chaining
	 */
	public Database refreshSearchIndex(boolean createIfDoesNotExist);

	/**
	 * Returns the server where the database is allocated. The String returned
	 * will depend on how is implemented the wrapper loaded.
	 * 
	 * @return the Database server
	 */
	public String getServer();

	/**
	 * Returns the file path where the database is allocated. The String
	 * returned will depend on how is implemented the wrapper loaded.
	 * 
	 * @return the Database file path
	 */
	public String getFilePath();

	/**
	 * Returns the database name. The String returned will depend on how is
	 * implemented the wrapper loaded.
	 * 
	 * @return the Database name
	 */
	public String getName();

}
