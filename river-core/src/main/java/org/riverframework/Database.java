package org.riverframework;

/**
 * Exposes the methods for control a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Database extends Base {
	/**
	 * Returns the Session that instanced the database
	 * @return a Session object. 
	 */
	public Session getSession();

	/**
	 * *
	 * Creates a document. The parameters needed will depend on what module is loaded.
	 * @param parameters
	 *            a set of Strings that will let the Database create a Document. 
	 * @return a new Document
	 */
	public Document createDocument(String... parameters);

	/**
	 * Returns an existent view. The parameters needed will depend on what module is being used.
	 * @param parameters
	 *            a set of Strings that will let the Database get an existent View. 
	 * @return a DefaultView object
	 */
	public View getView(String... parameters);

	/**
	 * Returns a View object instanced from the 'clazz' parameter. The clazz must inherit from DefaultView. The parameters needed will depend on what module is being used.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultView and implements the View interface.
	 * @param parameters
	 *            a set of Strings that will let the Database get an existent View. 
	 * @return a 'clazz' object
	 */
	public <U extends View> U getView(Class<U> clazz, String... parameters);

	/**
	 * Creates a Document object instanced from the 'clazz' parameter. The clazz must inherit from
	 * DefaultDocument. The parameters needed will depend on what module is being used.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param parameters
	 *            a set of Strings that will let the Database creates a new Document. 
	 * @return a 'clazz' object
	 */
	public <U extends Document> U createDocument(Class<U> clazz, String... parameters);

	/**
	 * Returns an existent Document. The parameters needed will depend on what module is being used.
	 * 
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent new Document. 
	 * @return the Document found. If no document is found, the method returns a closed Document object
	 */
	public Document getDocument(String... parameters);

	/**
	 * Returns a Document object instanced from the 'clazz' parameter. The clazz must inherit from
	 * DefaultDocument. The parameters needed will depend on what module is being used.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent new Document. 
	 * @return the Document found. If no document is found, the method returns a closed Document object
	 */
	public <U extends Document> U getDocument(Class<U> clazz, String... parameters);

	/**
	 * Returns a Document object instanced from the 'clazz' parameter. The clazz must inherit from
	 * DefaultDocument. The parameter createIfDoesNotExist lets to create the document if the method fails to found it. The parameters needed will depend on what module is being used.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param createIfDoesNotExist
	 *            if it's true and no document is found, the method returns a new document instanced from 'clazz'
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent new Document. 
	 * @return the Document found. If no document is found, and createIfDoesNotExist is false, the method returns a closed Document object
	 */
	public <U extends Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters);

	/**
	 * Creates a Document object instanced from the 'clazz' parameter, using a Module Document. The clazz must inherit from
	 * DefaultDocument.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param doc
	 *            an object Document from the module loaded in this time.
	 * @return a Document object using the doc provided as parameter.
	 */
	public <U extends Document> U getDocument(Class<U> clazz, org.riverframework.module.Document doc);

	/**
	 * Creates a Document object. The class is detected from the object doc using the rules write in the
	 * method detectClass
	 * 
	 * @param doc
	 *            an module Document object
	 * @return a Document object using the doc provided as parameter.
	 */
	public Document getDocument(org.riverframework.module.Document doc);

	/**
	 * Returns the class that getDocument(doc) must use following arbitrary conditions as some field from
	 * the doc object.
	 * 
	 * @param doc
	 *            an module Document object
	 * @return the class that must be used to instance it
	 */
	public Class<? extends org.riverframework.Document> detectClass(org.riverframework.module.Document doc);

	/**
	 * Returns all documents from the database as an DocumentList object, that implements List
	 * 
	 * @return a DocumentList document
	 */
	public DocumentList getAllDocuments();

	/**
	 * Returns the documents that match the query as a DocumentList object. The style of the query depends on how
	 * is implemented in the module loaded. For example, in IBM Notes is just something like "Black AND Dog"
	 * 
	 * @param query
	 * @return a DocumentList document
	 */
	public DocumentList search(String query);

	/**
	 * Refresh the database index. How this index is refreshed will depend on how is implemented in the module
	 * loaded.
	 * 
	 * @return the object itself for method chaining
	 */
	public Database refreshSearchIndex();

	/**
	 * Returns an Counter object to be used to generate autoincremental ids, for be used for implement the Unique interface.
	 * 
	 * @param key
	 *            the key for the counter
	 * @return a Counter object
	 */
	public Counter getCounter(String key);

	/**
	 * Returns true if the Module Database object was opened. If the Module Database is null or can't be opened, this method will
	 * returns false.
	 * 
	 * @return true if the module Database is opened
	 */
	public boolean isOpen();

	/**
	 * Returns the server where the database is allocated. The String returned will depend on how is
	 * implemented the module loaded.
	 * 
	 * @return the Database server
	 */
	public String getServer();

	/**
	 * Returns the file path where the database is allocated. The String returned will depend on how is
	 * implemented the module loaded.
	 * 
	 * @return the Database file path
	 */
	public String getFilePath();

	/**
	 * Returns the database name. The String returned will depend on how is
	 * implemented the module loaded.
	 * 
	 * @return the Database name
	 */
	public String getName();

}
