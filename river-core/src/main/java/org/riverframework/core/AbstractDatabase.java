package org.riverframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.ClosedObjectException;
import org.riverframework.Counter;
import org.riverframework.Database;
import org.riverframework.DocumentIterator;
import org.riverframework.RiverException;
import org.riverframework.Session;
import org.riverframework.Unique;

/**
 * It is the implementation for the interface Database. This class uses the Database wrapper loaded
 * at the Session creation. The Database lets you manage Documents, Counters, Views, and the Document's
 * searching.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractDatabase implements org.riverframework.Database {
	public static final String METHOD_GETINDEXNAME = "getIndexName";
	protected Session session = null;
	protected org.riverframework.wrapper.Database _database = null;

	protected AbstractDatabase(org.riverframework.Session s, org.riverframework.wrapper.Database _db) {
		session = s;
		_database = _db;
	}

	@Override
	public String getObjectId() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getObjectId();
	}

	@Override
	public org.riverframework.wrapper.Database getWrapperObject() {
		return _database;
	}

	@Override
	public org.riverframework.Session getSession() {
		return session;
	}

	@Override
	public String getServer() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getServer();
	}

	@Override
	public String getFilePath() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getFilePath();
	}

	@Override
	public String getName() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getName();
	}

	@Override
	public boolean isOpen() {
		return (_database != null && _database.isOpen());
	}

	@Override
	public org.riverframework.Document createDocument(String... parameters) {
		return createDocument(DefaultDocument.class, parameters);
	}

	@Override
	public <U extends org.riverframework.Document> U createDocument(Class<U> clazz, String... parameters) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		U doc = null;
		org.riverframework.wrapper.Document _doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (AbstractDocument.class.isAssignableFrom(clazz)) {
			_doc = _database.createDocument();

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
				constructor.setAccessible(true);
				doc = clazz.cast(constructor.newInstance(this, _doc));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		if (doc == null) {
			doc = clazz.cast(getDocument(clazz, (org.riverframework.wrapper.Document) null));
		} else {
			if (doc instanceof AbstractDocument && doc.isOpen()) {
				AbstractDocument<?> temp = (AbstractDocument<?>) doc;
				temp.afterCreate();
				temp.setModified(false);
			}
		}

		return doc;
	}

	@Override
	public org.riverframework.Document getDocument(String... parameters) {
		return getDocument(null, false, parameters);
	}

	@Override
	public <U extends org.riverframework.Document> U getDocument(Class<U> clazz, String... parameters) {
		return getDocument(clazz, false, parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist,
			String... parameters)
	{
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		U doc = null;
		org.riverframework.wrapper.Document _doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			_doc = _database.getDocument(id);

			if (!_doc.isOpen() && clazz != null && Unique.class.isAssignableFrom(clazz)) {
				String indexName = "";
				try {
					Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
					constructor.setAccessible(true);
					U uniqueDoc = clazz.cast(constructor.newInstance(this, null));
					Method method = clazz.getMethod(METHOD_GETINDEXNAME);
					method.setAccessible(true);
					indexName = (String) method.invoke(uniqueDoc);
				} catch (Exception e) {
					throw new RiverException(e);
				}

				if (indexName.equals(""))
					throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but its method "
							+ METHOD_GETINDEXNAME + " returns an empty string.");

				org.riverframework.wrapper.View indexView = _database.getView(indexName);

				if (indexView == null || !indexView.isOpen())
					throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but the index view "
							+ indexName + " does not exist.");

				indexView.refresh(); // TODO: this is EXPENSIVE!!
				_doc = indexView.getDocumentByKey(id);
			}

			if ((_doc == null || !_doc.isOpen()) && createIfDoesNotExist) {
				// Creating a new document
				if (clazz == null)
					throw new RiverException(
							"It's not possible create the document when the class parameter is null.");

				doc = createDocument(clazz, parameters);

				// If implements "Unique", setting the Id
				if (Unique.class.isAssignableFrom(clazz)) {
					((Unique) doc).setId(id);
				}
			} else {
				// Returning what was found
				if (clazz == null) {
					doc = (U) getDocument(DefaultDocument.class, _doc);
				} else {
					doc = clazz.cast(getDocument(clazz, _doc));
				}
			}
		} else {
			// There's no parameters. Returning a closed 'clazz' object
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
				constructor.setAccessible(true);
				doc = clazz.cast(constructor.newInstance(this, null));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		return doc;
	}

	@Override
	public Class<? extends org.riverframework.Document> detectClass(org.riverframework.wrapper.Document doc) {
		Class<? extends org.riverframework.Document> clazz = null;

		return clazz;
	}

	@Override
	public org.riverframework.Document getDocument(org.riverframework.wrapper.Document doc) {
		return getDocument(null, doc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.Document> U getDocument(Class<U> clazz, org.riverframework.wrapper.Document _doc) {
		U doc = null;
		Class<? extends org.riverframework.Document> c = clazz;

		if (c == null)
			c = detectClass(_doc);

		if (c == null)
			c = DefaultDocument.class;

		try {
			Constructor<?> constructor = c.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
			constructor.setAccessible(true);
			doc = (U) constructor.newInstance(this, _doc);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return doc;
	}

	@Override
	public org.riverframework.View getView(String... parameters) {
		return getView(DefaultView.class, parameters);
	}

	@Override
	public <U extends org.riverframework.View> U getView(Class<U> clazz, String... parameters) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		U view = null;
		org.riverframework.wrapper.View _view = null;
		String id = parameters[0];

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		_view = _database.getView(id);

		if (AbstractView.class.isAssignableFrom(clazz)) {
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.View.class);
				constructor.setAccessible(true);
				view = clazz.cast(constructor.newInstance(this, _view));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		return view;
	}

	@Override
	public DocumentIterator getAllDocuments() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.DocumentIterator _iterator = _database.getAllDocuments();
		DocumentIterator result = new DefaultDocumentIterator(this, _iterator);

		return result;
	}

	@Override
	public DocumentIterator search(String query) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.DocumentIterator _iterator = _database.search(query);
		DocumentIterator result = new DefaultDocumentIterator(this, _iterator);

		return result;
	}

	@Override
	public org.riverframework.Database refreshSearchIndex() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		_database.refreshSearchIndex();
		return this;
	}

	@Override
	public org.riverframework.Counter getCounter(String key) {
		Counter counter = getDocument(DefaultCounter.class, true, key);

		return counter;
	}

	@Override
	public void close() {
		_database.close();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
