package org.riverframework.core;

//TODO: evaluate if this class can be a Document or Database index, more than a class itself. 
public interface View extends Base {

  /**
   * @return the Database where the View is allocated
   */
  public Database getDatabase();

  /**
   * Returns the object that wraps the native object. For example, if the wrapper loaded is
   * River.LOTUS_DOMINO, and the object is an instance of org.riverframework.core.DefaultDocument,
   * getNativeObject() will return an object that implements the org.riverframework.wrapper.Document
   * interface.
   *
   * @return the object used to wrap the native object
   */
  @Override
  public org.riverframework.wrapper.View<?> getWrapperObject();

  /**
   * Searches by the key and returns a Document object. If nothing is found, it will be an object
   * closed.
   *
   * @param key the document's key
   * @return a Document object.
   */
  public Document getDocumentByKey(String key);

  /**
   * Searches by the key and returns a 'clazz' instance. If nothing is found, it will be an object
   * closed.
   *
   * @param <U> a class that inherits from AbstractDocument and implements Document.
   * @param clazz a class object that inherits from AbstractDocument and implements Document.
   * @param key the document's key
   * @return a core Document object. If nothing is found, it will be an object closed.
   */
  public <U extends org.riverframework.core.AbstractDocument<?>> U getDocumentByKey(Class<U> clazz,
      String key);

  /**
   * Returns a core DocumentIterator object with all the documents from the database.
   *
   * @return a core DocumentIterator object
   */
  public DocumentIterator getAllDocuments();

  /**
   * Searches by the key and returns a DocumentIterator object with all the documents that match.
   *
   * @param key the document's key
   * @return a core DocumentIterator object
   */
  public DocumentIterator getAllDocumentsByKey(Object key);

  /**
   * Refresh the view index. This method could be expensive depending on the wrapper used. It must
   * be used with care.
   *
   * @return this View, for method chaining
   */
  public View refresh();

  /**
   * Deletes the current view. The function isOpen will return false after call it.
   */
  public void delete();

  /**
   * Searches by the query and returns a DocumentList object with the documents that match. So far,
   * The style of the query will depend on how is implemented in the wrapper loaded. For example,
   * with the lotus.domino wrapper, it will be something like "Black AND Dog"
   *
   * @param query the query in the wrapper's format
   * @return a DocumentIterator document
   */
  public DocumentIterator search(String query);

  /**
   * Searches by the query and returns a DocumentList object with the documents that match. So far,
   * The style of the query will depend on how is implemented in the wrapper loaded. For example,
   * with the lotus.domino wrapper, it will be something like "Black AND Dog". The max parameter
   * will indicate how much documents could retrieve the method. It will depend on what wrapper is
   * being used.
   *
   * @param query the query in the wrapper's format
   * @param max the maximum number of documents retrieved by the method. A zero value will retrieve
   * all that the wrapper allows to do it.
   * @return a DocumentIterator document
   */
  public DocumentIterator search(String query, int max);

}
