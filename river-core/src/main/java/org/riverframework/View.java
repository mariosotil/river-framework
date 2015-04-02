package org.riverframework;

//TODO: evaluate if this class can be a Document or Database index, more than a class itself. 
public interface View extends Base {
	/**
	 * @return the Database where the View is allocated
	 */
	public Database getDatabase();

	/**
	 * If the core View object is created, but the module View is null or can't be opened, this method will
	 * return false.
	 * 
	 * @return true if the module Database is opened
	 */
	public boolean isOpen();

	/**
	 * This method returns a core Document searching by the parameter key.
	 * 
	 * @param key
	 * @return a core Document object. If nothing is found, it will be an object closed.
	 */
	public Document getDocumentByKey(String key);

	/**
	 * This method returns a core Document searching for the key.
	 *
	 * @param clazz
	 *            a class that inherits from DefaultDocument and implements Document.
	 * @param key
	 * @return a core Document object. If nothing is found, it will be an object closed.
	 */
	public <U extends Document> U getDocumentByKey(Class<U> clazz, String key);

	/**
	 * This method returns a core DocumentCollection object with all the documents as core Document.
	 * 
	 * @return a core DocumentCollection object
	 */
	public DocumentCollection getAllDocuments();

	/**
	 * This method returns a core DocumentCollection object with all the documents as core Document, searching by the
	 * parameter key..
	 * 
	 * @return a core DocumentCollection object
	 */
	public DocumentCollection getAllDocumentsByKey(Object key);

	/**
	 * This method force the view to refresh its index. Its behavior will depend on how the module loaded is
	 * implemented.
	 * 
	 * @return
	 */
	public View refresh();

	/**
	 * Returns the documents that match the query as a DocumentCollection object. The style of the query depends on how
	 * is implemented in the module loaded. For example, in IBM Notes is just something like "Black AND Dog"
	 * 
	 * @param query
	 * @return a DocumentCollection document
	 */
	public DocumentCollection search(String query);
}
