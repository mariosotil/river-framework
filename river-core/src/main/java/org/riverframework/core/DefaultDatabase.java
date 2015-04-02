package org.riverframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.Base;
import org.riverframework.Counter;
import org.riverframework.Database;
import org.riverframework.DocumentCollection;
import org.riverframework.RiverException;
import org.riverframework.Session;
import org.riverframework.Unique;

public class DefaultDatabase implements org.riverframework.Database {
	public static final String METHOD_GETINDEXNAME = "getIndexName";
	protected Session session = null;
	protected org.riverframework.module.Database _database = null;

	protected DefaultDatabase(org.riverframework.Session s, org.riverframework.module.Database _db) {
		session = s;
		_database = _db;
	}

	@Override
	public String getObjectId() {
		return _database.getObjectId();
	}

	@Override
	public Base getParent() {
		return session;
	}

	@Override
	public Object getModuleObject() {
		return _database;
	}

	@Override
	public org.riverframework.Session getSession() {
		return session;
	}

	@Override
	public String getServer() {
		return _database.getServer();
	}

	@Override
	public String getFilePath() {
		return _database.getFilePath();
	}

	@Override
	public String getName() {
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
		U rDoc = null;
		org.riverframework.module.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (DefaultDocument.class.isAssignableFrom(clazz)) {
			doc = _database.createDocument();

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.module.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, doc));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		if (rDoc == null) {
			rDoc = clazz.cast(getDocument(clazz, (org.riverframework.module.Document) null));
		} else {
			// TODO: IMPROVE this! It should not be need to cast to call afterCreate and setModified should not be in
			// the Document interface
			((DefaultDocument) rDoc).afterCreate().setModified(false);
		}

		return rDoc;
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
		U doc = null;
		org.riverframework.module.Document _doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			_doc = _database.getDocument(id);

			if (!_doc.isOpen() && clazz != null && Unique.class.isAssignableFrom(clazz)) {
				Method method = null;
				try {
					method = clazz.getMethod(METHOD_GETINDEXNAME);
				} catch (Exception e) {
					throw new RiverException(e);
				}

				if (method == null)
					throw new RiverException("The class " + clazz.getSimpleName()
							+ " implements Unique but it does not implement the method " + METHOD_GETINDEXNAME + ".");

				String indexName = "";

				try {
					method.setAccessible(true);
					indexName = (String) method.invoke(null);
				} catch (Exception e) {
					throw new RiverException(e);
				}

				if (indexName.equals(""))
					throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but its method "
							+ METHOD_GETINDEXNAME + " returns an empty string.");

				org.riverframework.module.View indexView = _database.getView(indexName);

				if (indexView == null)
					throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but the index view "
							+ indexName + " does not exist.");

				indexView.refresh(); // TODO: this is EXPENSIVE!
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
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.module.Document.class);
				constructor.setAccessible(true);
				doc = clazz.cast(constructor.newInstance(this, null));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		return doc;
	}

	@Override
	public Class<? extends org.riverframework.Document> detectClass(org.riverframework.module.Document doc) {
		Class<? extends org.riverframework.Document> clazz = null;

		return clazz;
	}

	@Override
	public org.riverframework.Document getDocument(org.riverframework.module.Document doc) {
		return getDocument(null, doc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.Document> U getDocument(Class<U> clazz, org.riverframework.module.Document _doc) {
		U doc = null;
		Class<? extends org.riverframework.Document> c = clazz;

		if (c == null)
			c = detectClass(_doc);

		if (c == null)
			c = DefaultDocument.class;

		try {
			Constructor<?> constructor = c.getDeclaredConstructor(Database.class, org.riverframework.module.Document.class);
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
		U view = null;
		org.riverframework.module.View _view = null;
		String id = parameters[0];

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		_view = _database.getView(id);

		if (DefaultView.class.isAssignableFrom(clazz)) {
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.riverframework.module.View.class);
				constructor.setAccessible(true);
				view = clazz.cast(constructor.newInstance(this, _view));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		return view;
	}

	@Override
	public org.riverframework.DocumentCollection getAllDocuments() {
		org.riverframework.module.DocumentCollection _col;
		_col = _database.getAllDocuments();
		DocumentCollection result = new DefaultDocumentCollection(this).loadFrom(_col);

		return result;
	}

	@Override
	public org.riverframework.DocumentCollection search(String query) {
		org.riverframework.module.DocumentCollection _col;
		_col = _database.search(query);
		DocumentCollection result = new DefaultDocumentCollection(this).loadFrom(_col);

		return result;
	}

	@Override
	public org.riverframework.Database refreshSearchIndex() {
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
