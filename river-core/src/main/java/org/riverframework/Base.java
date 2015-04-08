package org.riverframework;

/**
 * It is used to define some common methods to Session, Database, Document and View interfaces.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Base {
	/**
	 * Returns the id from the object instanced. Its behavior will depend on how the module loaded is implemented.
	 * 
	 * @return its id.
	 */
	public String getObjectId();

	/**
	 * Returns the object created through the module loaded. You should not need to use it.
	 * 
	 * @return the object created from the module
	 */
	public Object getModuleObject();

	/**
	 * Close the resources, handlers, etc. opened by this instance.
	 */
	public void close();
}
