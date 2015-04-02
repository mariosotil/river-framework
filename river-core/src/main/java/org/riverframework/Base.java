package org.riverframework;

/**
 * The Base interface is used to define some common methods to Session, Database, Document and View interfaces.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Base {
	/**
	 * Returns an Id from the object instanced. This could be very different, and it will depend about the module used
	 * in that moment.
	 * 
	 * @return an string Id.
	 */
	public String getObjectId();

	/**
	 * Returns the object that instanced this one. For example, for a database, this function will return a Session
	 * object.
	 * 
	 * @return the object that instanced this one.
	 */
	public Base getParent();

	/**
	 * Returns the object created from the module. You should not need to use it.
	 * 
	 * @return the object created from the module
	 */
	public Object getModuleObject();

	/**
	 * This method should close the resources, handlers, etc. opened by the instance.
	 */
	public void close();
}
