package org.riverframework.extended;

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

	public boolean isOpen();
	
	/**
	 * Close the resources, handlers, etc. opened by this instance.
	 */
	public void close();
}
