package org.riverframework.module;

/**
 * It is used to define some common methods between the module's classes. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Base {
	/**
	 * Returns the object wrapped by the module class. Considering that a module wraps a library that 
	 * it would be used to access natively to the NoSQL server, it is very possible that the module does not have
	 * specific operations. In that case, to have access to the referenced object will allow to do it. 
	 *  
	 * @return the object wrapped
	 */
	public Object getReferencedObject();

	/**
	 * Returns an id that identifies the object wrapped. It will depend on the module implementation.
	 *  
	 * @return an id
	 */
	public String getObjectId();

	/**
	 * Closes the resources, handles, etc.&nbsp;opened by this object. 
	 */
	public void close();
}
