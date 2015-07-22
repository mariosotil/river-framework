package org.riverframework.core;

import org.riverframework.ClosedObjectException;

/**
 * It is used to manage Databases by default, if we don't need to create a class for each database accessed.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractIndexedDatabase<T extends AbstractIndexedDatabase<T>> extends AbstractDatabase<T>
implements IndexedDatabase {

	protected AbstractIndexedDatabase(Session session, org.riverframework.wrapper.Database<?> _database) {
		super(session, _database);
	}

	@Override
	public DefaultCounter getCounter(String key) {
		DefaultCounter counter = getDocument(DefaultCounter.class, key);

		if (!counter.isOpen()) {
			counter = createDocument(DefaultCounter.class, key);
		}
		return counter;
	}

	@Override
	public <U extends AbstractDocument<?>> View getIndex(Class<U> clazz) {
		if (IndexedDocument.class.isAssignableFrom(clazz)) {
			return ((IndexedDocument<?>) getClosedDocument(clazz)).getIndex();
		} else {
			return getClosedView();
		}
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
			// TODO: maybe it would be necessary to allow more arguments as key
			doc = clazz.cast(index.getDocumentByKey(clazz, parameters[0]));
		}

		if (doc == null || !doc.isOpen()) {
			doc = super.getDocument(clazz, createIfDoesNotExist, parameters);

			if (createIfDoesNotExist && IndexedDocument.class.isAssignableFrom(clazz) && doc != null && doc.isOpen()) {
				IndexedDocument<?> idxDoc = ((IndexedDocument<?>) doc);

				if (idxDoc.getId().equals(""))
					idxDoc.setId(parameters[0]);
			}
		}

		return doc;
	}
}
