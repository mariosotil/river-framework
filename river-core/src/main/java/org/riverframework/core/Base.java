package org.riverframework.core;

/**
 * It is used to define some common methods to Session, Database, Document and View interfaces.
 *
 * @author mario.sotil@gmail.com
 */
public interface Base {

  /**
   * Returns the id from the object instanced. Its behavior will depend on how the wrapper loaded is
   * implemented.
   *
   * @return its id.
   */
  public String getObjectId();

  /**
   * Returns the object that wraps the native object. For example, if the wrapper loaded is
   * River.LOTUS_DOMINO, and the object is an instance of org.riverframework.core.DefaultDocument,
   * getNativeObject() will return an object that implements the org.riverframework.wrapper.Document
   * interface.
   *
   * @return the object used to wrap the native object
   */
  org.riverframework.wrapper.Base<?> getWrapperObject();

  /**
   * Returns the object wrapped by the wrapper class. Considering that a wrapper wraps a library
   * that it would be used to access natively to the NoSQL server, it is very possible that the core
   * object does not have specific operations. In that case, the access to the referenced object
   * will allow to do it.
   *
   * @return the object wrapped
   */
  public Object getNativeObject();

  /**
   * Returns if the wrapper was successful opened
   *
   * @return the result
   */
  public boolean isOpen();

  /**
   * Close the resources, handlers, etc. opened by this instance.
   */
  public void close();
}
