package org.riverframework.core;

/**
 * It is used to manage Databases by default, if we don't need to create a class for each database accessed. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultDatabase extends AbstractDatabase implements org.riverframework.Database {
	protected DefaultDatabase(org.riverframework.Session s, org.riverframework.wrapper.Database<?> _db) {
		super(s, _db);
	}

}
