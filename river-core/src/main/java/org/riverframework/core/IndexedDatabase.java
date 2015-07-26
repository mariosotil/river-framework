package org.riverframework.core;

/**
 * Exposes the methods for control a NoSQL database.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public interface IndexedDatabase extends Database {
	public DefaultIndex createIndex(String key, String... parameters);

	public DefaultIndex getIndex(String key);

	public IndexedDatabase initCounter();

	public DefaultCounter getCounter(String key);

	public <U extends AbstractDocument<?>> View getIndex(Class<U> clazz);

	public Class<? extends AbstractDocument<?>> getClassFromDocument(org.riverframework.wrapper.Document<?> _doc);
}
