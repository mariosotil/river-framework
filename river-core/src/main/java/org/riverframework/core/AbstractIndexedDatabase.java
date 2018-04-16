package org.riverframework.core;

import java.util.HashMap;
import org.riverframework.ClosedObjectException;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;

/**
 * It is used to manage Databases by default, if we don't need to create a class for each database
 * accessed.
 *
 * @author mario.sotil@gmail.com
 */
public abstract class AbstractIndexedDatabase<T extends AbstractIndexedDatabase<T>> extends
    AbstractDatabase<T>
    implements IndexedDatabase {

  private final HashMap<String, Class<? extends AbstractDocument<?>>> classes =
      new HashMap<String, Class<? extends AbstractDocument<?>>>();
  private final HashMap<String, View> indexes = new HashMap<String, View>();

  protected AbstractIndexedDatabase(Session session, Database<?> _database) {
    super(session, _database);
  }

  @Override
  public <U extends AbstractDocument<?>> T registerDocumentClass(Class<U> clazz) {

    if (isOpen()) {
      Document closedDoc = getClosedDocument(clazz);
      // Saving the table name and the class associated in the cache
      classes.put(closedDoc.getBinder(), clazz);

      // If it is an indexed document class...
      if (AbstractIndexedDocument.class.isAssignableFrom(clazz)) {
        String key = clazz.getName();

        // ... and its index is not loaded yet ...
        if (indexes.get(key) == null) {

          // ...save it in the cache
          IndexedDocument<?> closedIndexedDoc = (IndexedDocument<?>) closedDoc;

          View index = getView(closedIndexedDoc.getIndexName());

          if (index == null || !index.isOpen()) {
            try {
              index = closedIndexedDoc.createIndex();
            } catch (Exception e) {
              index = null;
            }
          }

          if (index == null || !index.isOpen()) {
            throw new RiverException(
                "It could not be possible load the index for the class " + key);
          }

          indexes.put(key, index);
        }
      }
    }

    return getThis();
  }

  @Override
  public <U extends AbstractDocument<?>> View getIndex(Class<U> clazz) {
    if (IndexedDocument.class.isAssignableFrom(clazz)) {
      String key = clazz.getName();
      View index = indexes.get(key);

      if (index == null) {
        index = getClosedView();

      } else if (index.isOpen()) {
        // We always need the index updated
        index.refresh();

      }

      return index;

    } else {
      return getClosedView();
    }
  }

  @Override
  public DefaultCounter getCounter(String key) {
    DefaultCounter counter = getDocument(DefaultCounter.class, key);

    if (!counter.isOpen()) {
      counter = createDocument(DefaultCounter.class, key).setId(key)
          .save();
    }
    return counter;
  }

  @Override
  public Document getDocument(org.riverframework.wrapper.Document<?> _doc) {
    String tableName = _doc.getBinder();
    Class<? extends AbstractDocument<?>> clazz = classes.get(tableName);

    if (clazz == null) {
      clazz = DefaultDocument.class;
    }

    Document doc = getDocument(clazz, _doc);
    return doc;
  }

  @Override
  public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz, boolean createIfDoesNotExist,
      String... parameters) {
    if (!isOpen()) {
      throw new ClosedObjectException("The Session object is closed.");
    }

    U doc = null;

    View index = getIndex(clazz);
    if (index.isOpen()) {
      doc = clazz.cast(index.getDocumentByKey(clazz, parameters[0]));
    }

    if (doc == null || !doc.isOpen()) {
      doc = super.getDocument(clazz, createIfDoesNotExist, parameters);

      if (createIfDoesNotExist && IndexedDocument.class.isAssignableFrom(clazz) && doc != null
          && doc.isOpen()) {
        IndexedDocument<?> idxDoc = ((IndexedDocument<?>) doc);

        if (idxDoc.getId()
            .equals("")) {
          idxDoc.setId(parameters[0]);
        }
      }
    }

    return doc;
  }
}
