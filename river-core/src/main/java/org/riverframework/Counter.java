package org.riverframework;

/**
 * This class let you get counters to be used to assign incremental Ids to the documents. It's used with the Unique
 * interface.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface Counter extends org.riverframework.Document {
	/**
	 * Every time this method is called, it returns a new number in incremental way.
	 * 
	 * @return a new number incremented by one.
	 */
	public long getCount();
}
