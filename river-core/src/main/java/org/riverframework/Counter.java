package org.riverframework;

/**
 * Get counters to be used to assign incremental ids to the documents. It's used with the Unique
 * interface.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Counter extends org.riverframework.Document {
	/**
	 * Returns a new number on an incremental way. 
	 * 
	 * @return a number.
	 */
	public long getCount();
}
