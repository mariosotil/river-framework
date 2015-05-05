package org.riverframework;

/**
 * It is used to define some common methods to Session, Database, Document and View interfaces.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Base {
	/**
	 * Returns the id from the object instanced. Its behavior will depend on how the wrapper loaded is implemented.
	 * 
	 * @return its id.
	 */
	public String getObjectId();

	/**
	 * Returns the object that wraps the native object. For example, if the wrapper loaded is
	 * River.LOTUS_DOMINO, and the object is an instance of org.riverframework.core.DefaultDocument,
	 * getNativeObject() will return an object that implements the org.riverframework.wrapper.Document interface.
	 * 
	 * @return the object used to wrap the native object
	 */
	public Object getWrapperObject();

	/**
	 * Close the resources, handlers, etc. opened by this instance.
	 */
	public void close();
}
