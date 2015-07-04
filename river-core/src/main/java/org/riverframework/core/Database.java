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

	public <U extends org.riverframework.extended.AbstractDocument<?>> U createDocument(Class<U> clazz, String... parameters);

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

	public Document getDocument(boolean createIfDoesNotExist, String... parameters);
	
	/**
	 * Creates a Document object. The class is detected from the object doc
	 * using the rules write in the method detectClass
	 * 
	 * @param doc
	 *            an wrapper Document object
	 * @return a Document object using the doc provided as parameter.
	 */
	public Document getDocument(org.riverframework.wrapper.Document<?> doc);

	public <U extends org.riverframework.extended.AbstractDocument<?>> U getDocument(Class<U> clazz, String... parameters);

	public <U extends org.riverframework.extended.AbstractDocument<?>> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters);

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
	 * @return a DocumentIterator document
	 */
	public DocumentIterator search(String query);

	/**
	 * Refresh the database index. How this index is refreshed will depend on
	 * how is implemented in the wrapper loaded.
	 * 
	 * @return the object itself for method chaining
	 */
	public Database refreshSearchIndex();

	/**
	 * Returns true if the wrapper Database object was opened. If the wrapper
	 * Database is null or can't be opened, this method will returns false.
	 * 
	 * @return true if the wrapper Database is opened
	 */
	public boolean isOpen();

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
