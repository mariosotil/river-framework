package org.riverframework;

//TODO: evaluate if this class can be a Document or Database index, more than a class itself. 
public interface View extends Base {
	/**
	 * @return the Database where the View is allocated
	 */
	public Database getDatabase();

	/**
	 * Returns true if the wrapper View was opened,is null or can't be opened, this method wi
	 * 
	 * @return true if the wrapper Database is opened
	 */
	public boolean isOpen();

	/**
	 * Searches by the key and returns a Document object. If nothing is found, it will be an object closed.
	 * 
	 * @param key
	 * @return a Document object. 
	 */
	public Document getDocumentByKey(String key);

	/**
	 * Searches by the key and returns a 'clazz' instance. If nothing is found, it will be an object closed.
	 *
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param key
	 * @return a core Document object. If nothing is found, it will be an object closed.
	 */
	public <U extends Document> U getDocumentByKey(Class<U> clazz, String key);

	/**
	 * Returns a core DocumentList object with all the documents from the database.
	 * 
	 * @return a core DocumentList object
	 */
	public DocumentList getAllDocuments();

	/**
	 * Searches by the key and returns a DocumentList object with all the documents that match. 
	 * @return a core DocumentList object
	 */
	public DocumentList getAllDocumentsByKey(Object key);

	/**
	 * Refresh the view index. This method could be expensive depending on the wrapper used. It must be used with care.
	 * 
	 * @return this View, for method chaining
	 */
	public View refresh();

	/**
	 * Searches by the query and returns a DocumentList object with the documents that match. So far, The style of the query will depend on how
	 * is implemented in the wrapper loaded. For example, in IBM Notes is just something like "Black AND Dog"
	 * 
	 * @param query
	 * @return a DocumentList document
	 */
	public DocumentList search(String query);
}
