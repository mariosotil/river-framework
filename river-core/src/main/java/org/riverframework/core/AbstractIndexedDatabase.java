package org.riverframework.core;

import java.util.concurrent.ConcurrentHashMap;

import org.riverframework.ClosedObjectException;
import org.riverframework.wrapper.Database;

/**
 * It is used to manage Databases by default, if we don't need to create a class for each database accessed.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractIndexedDatabase<T extends AbstractIndexedDatabase<T>> extends AbstractDatabase<T>
		implements IndexedDatabase {

	final static ConcurrentHashMap<String, View> indexes = new ConcurrentHashMap<String, View>();

	protected AbstractIndexedDatabase(Session session, Database<?> _database) {
		super(session, _database);
	}

	@Override
	public <U extends AbstractDocument<?>> View createIndex(Class<U> clazz) {
		if (IndexedDocument.class.isAssignableFrom(clazz)) {
			View index = getIndex(clazz);

			if (index == null || !index.isOpen()) {
				index = ((IndexedDocument<?>) getClosedDocument(clazz)).createIndex();
			}

			if (index == null) {
				index = getClosedView();
			}

			return index;
		} else {
			return getClosedView();
		}
	}

	@Override
	public <U extends AbstractDocument<?>> View getIndex(Class<U> clazz) {
		if (IndexedDocument.class.isAssignableFrom(clazz)) {
			String key = clazz.getName();
			View index = indexes.get(key);

			if (index == null || !index.isOpen()) {
				index = ((IndexedDocument<?>) getClosedDocument(clazz)).getIndex();

				if (index != null && index.isOpen())
					indexes.put(key, index);
			}

			if (index == null) {
				index = getClosedView();
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
	public Class<? extends AbstractDocument<?>> getClassFromDocument(org.riverframework.wrapper.Document<?> _doc) {
		return DefaultDocument.class;
	}

	@Override
	public Document getDocument(org.riverframework.wrapper.Document<?> _doc) {
		Class<? extends AbstractDocument<?>> clazz = getClassFromDocument(_doc);

		Document doc = getDocument(clazz, _doc);
		return doc;
	}

	@Override
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz, boolean createIfDoesNotExist,
			String... parameters) {
		if (!isOpen())
			throw new ClosedObjectException("The Session object is closed.");

		U doc = null;

		View index = getIndex(clazz);
		if (index.isOpen()) {
			doc = clazz.cast(index.getDocumentByKey(clazz, parameters[0]));
		}

		if (doc == null || !doc.isOpen()) {
			doc = super.getDocument(clazz, createIfDoesNotExist, parameters);

			if (createIfDoesNotExist && IndexedDocument.class.isAssignableFrom(clazz) && doc != null && doc.isOpen()) {
				IndexedDocument<?> idxDoc = ((IndexedDocument<?>) doc);

				if (idxDoc.getId()
							.equals(""))
					idxDoc.setId(parameters[0]);
			}
		}

		return doc;
	}
}
