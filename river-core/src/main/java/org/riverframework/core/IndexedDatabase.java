package org.riverframework.core;

/**
 * Exposes the methods for control a NoSQL database.
 *
 * @author mario.sotil@gmail.com
 */
public interface IndexedDatabase extends Database {

  public <U extends AbstractDocument<?>> View getIndex(Class<U> clazz);

  public DefaultCounter getCounter(String key);

  public <U extends AbstractDocument<?>> IndexedDatabase registerDocumentClass(Class<U> clazz);

}
