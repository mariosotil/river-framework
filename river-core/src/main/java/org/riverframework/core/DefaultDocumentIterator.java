package org.riverframework.core;

/**
 * It is used to manage collections of Documents.
 *
 * @author mario.sotil@gmail.com
 */
public final class DefaultDocumentIterator implements org.riverframework.core.DocumentIterator {

  protected org.riverframework.wrapper.DocumentIterator<?, ?> _iterator;
  private Database database;

  protected DefaultDocumentIterator(Database d,
      org.riverframework.wrapper.DocumentIterator<?, ?> _iterator) {
    this.database = d;
    this._iterator = _iterator;
  }

  @Override
  public Database getDatabase() {
    return database;
  }

  @Override
  public Document next() {
    org.riverframework.wrapper.Document<?> _doc = _iterator.next();
    org.riverframework.core.Document doc = database.getDocument(_doc);

    return doc;
  }

  @Override
  public boolean hasNext() {
    return _iterator.hasNext();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public DocumentIterator iterator() {
    return this;
  }

  @Override
  public boolean isOpen() {
    return (_iterator != null);
  }

  @Override
  public String getObjectId() {
    // if (!isOpen())
    // throw new ClosedObjectException("The Document object is closed.");

    String result = _iterator.getObjectId();
    return result;
  }

  @Override
  public org.riverframework.wrapper.DocumentIterator<?, ?> getWrapperObject() {
    return _iterator;
  }

  @Override
  public Object getNativeObject() {
    return _iterator.getNativeObject();
  }

  @Override
  public String toString() {
    return getClass().getName() + "(" + getWrapperObject().toString() + ")";
  }

  @Override
  public DocumentIterator deleteAll() {
    _iterator.deleteAll();
    return this;
  }

  @Override
  public void close() {
    _iterator.close();
  }
}
