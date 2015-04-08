package org.riverframework.core;

/**
 * It is used to manage a session using a NoSQL module from this framework. Allows access to databases.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultSession extends AbstractSession implements org.riverframework.Session {
	protected DefaultSession(org.riverframework.module.Session _s) {
		// Exists only to defeat instantiation.
		super(_s);
	}
}
