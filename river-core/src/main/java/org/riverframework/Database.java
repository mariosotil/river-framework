package org.riverframework;

/**
 * This interface exposes the methods for control a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Database extends Base {
	/**
	 * @return the Session that instanced the database
	 */
	public Session getSession();

	/**
	 * *
	 * 
	 * @param parameters
	 *            a set of Strings that will let the Database create a Document. The strings needed will depend on what
	 *            module is being used.
	 * @return a new Document
	 */
	public Document createDocument(String... parameters);

	/**
	 * @param parameters
	 *            a set of Strings that will let the Database get an existent View. The strings needed will depend on
	 *            what module is being used.
	 * @return a DefaultView object
	 */
	public View getView(String... parameters);

	/**
	 * This method returns a View object instanced from the 'clazz' parameter. The clazz must inherit from DefaultView.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultView and implements the core View interface.
	 * @param parameters
	 *            a set of Strings that will let the Database get an existent View. The strings needed will depend on
	 *            what module is being used.
	 * @return a 'clazz' object
	 */
	public <U extends View> U getView(Class<U> clazz, String... parameters);

	/**
	 * This method creates a Document object instanced from the 'clazz' parameter. The clazz must inherit from
	 * DefaultDocument.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param parameters
	 *            a set of Strings that will let the Database creates a new Document. The strings needed will depend on
	 *            what module is being used.
	 * @return
	 */
	public <U extends Document> U createDocument(Class<U> clazz, String... parameters);

	/**
	 * This method retrieve an existent Document using the parameters selected.
	 * 
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent new Document. The strings needed will
	 *            depend on what module is being used.
	 * @return the Document found. If no document is found, the method returns a closed Document object
	 */
	public Document getDocument(String... parameters);

	/**
	 * This method retrieve a Document object instanced from the 'clazz' parameter. The clazz must inherit from
	 * DefaultDocument.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent new Document. The strings needed will
	 *            depend on what module is being used.
	 * @return the Document found. If no document is found, the method returns a closed Document object
	 */
	public <U extends Document> U getDocument(Class<U> clazz, String... parameters);

	/**
	 * 
	 * This method retrieve a Document object instanced from the 'clazz' parameter. The clazz must inherit from
	 * DefaultDocument. If the
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param createIfDoesNotExist
	 *            if it's true and no document is found, the method returns a new document instanced from 'clazz'
	 * @param parameters
	 *            a set of Strings that will let the Database locate an existent new Document. The strings needed will
	 *            depend on what module is being used.
	 * @return the Document found. If no document is found, the method returns a closed Document object
	 */
	public <U extends Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters);

	/**
	 * This method creates a Document object instanced from the 'clazz' parameter. The clazz must inherit from
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
	 * This method creates a Document object. The class is detected from the object doc using the rules write in the
	 * method detectClass
	 * 
	 * @param doc
	 *            an module Document object
	 * @return a Document object using the doc provided as parameter.
	 */
	public Document getDocument(org.riverframework.module.Document doc);

	/**
	 * This method returns the class that getDocument(doc) must use following arbitrary conditions as some field from
	 * the doc object
	 * 
	 * @param doc
	 *            an module Document object
	 * @return the class that must be used to instance it
	 */
	public Class<? extends org.riverframework.Document> detectClass(org.riverframework.module.Document doc);

	/**
	 * Returns all documents from the database as an DocumentCollection object, that implements List
	 * 
	 * @return a DocumentCollection document
	 */
	public DocumentCollection getAllDocuments();

	/**
	 * Returns the documents that match the query as a DocumentCollection object. The style of the query depends on how
	 * is implemented in the module loaded. For example, in IBM Notes is just something like "Black AND Dog"
	 * 
	 * @param query
	 * @return a DocumentCollection document
	 */
	public DocumentCollection search(String query);

	/**
	 * It refresh the database index. How this index is refreshed will depend on how is implemented in the module
	 * loaded.
	 * 
	 * @return the object intself for method chaining
	 */
	public Database refreshSearchIndex();

	/**
	 * This method returns an Counter object to be used to generate autoincremental ids, for example, when it's
	 * implemented the Unique interface.
	 * 
	 * @param key
	 *            the key for the counter
	 * @return a Counter object
	 */
	public Counter getCounter(String key);

	/**
	 * If the core Database object is created, but the module Database is null or can't be opened, this method will
	 * return false.
	 * 
	 * @return true if the module Database is opened
	 */
	public boolean isOpen();

	/**
	 * This method returns the server where the database is allocated. The String returned will depend on how is
	 * implemented the module loaded.
	 * 
	 * @return the Database server
	 */
	public String getServer();

	/**
	 * This method returns the file path where the database is allocated. The String returned will depend on how is
	 * implemented the module loaded.
	 * 
	 * @return the Database file path
	 */
	public String getFilePath();

	/**
	 * This method returns the database name. The String returned will depend on how is
	 * implemented the module loaded.
	 * 
	 * @return the Database name
	 */
	public String getName();

}
