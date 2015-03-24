package org.riverframework.domino;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.riverframework.RiverException;

public class DefaultDatabase implements org.riverframework.domino.Database {
	public static final String METHOD_GETINDEXNAME = "getIndexName";
	protected Session session = null;
	protected org.openntf.domino.Database _db = null;

	protected DefaultDatabase(org.riverframework.domino.Session s, org.openntf.domino.Database obj) {
		session = s;
		_db = obj;
	}
	
	@Override
	public org.riverframework.domino.Session getSession() {
		return session;
	}

	@Override
	public String getServer() {
		return _db.getServer();
	}

	@Override
	public String getFilePath() {
		return _db.getFilePath();
	}

	@Override
	public String getName() {
		return _db.getTitle();
	}

	@Override
	public boolean isOpen() {
		return (_db != null && _db.isOpen());
	}

	@Override
	public Database getMainReplica() {
		String adminServer = "";
		String replicaId = "";

		adminServer = _db.getACL().getAdministrationServer();
		if (adminServer.equals(""))
			return this;

		replicaId = _db.getReplicaID();

		DefaultDatabase d = session.getDatabase(DefaultDatabase.class, adminServer, replicaId);

		return d;
	}

	@Override
	public org.riverframework.domino.Document createDocument(String... parameters) {
		return createDocument(DefaultDocument.class, parameters);
	}

	@Override
	public <U extends org.riverframework.domino.Document> U createDocument(Class<U> clazz, String... parameters) {
		U rDoc = null;
		org.openntf.domino.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (DefaultDocument.class.isAssignableFrom(clazz)) {
			doc = _db.createDocument();
			doc.replaceItemValue(org.riverframework.Document.FIELD_CLASS, clazz.getCanonicalName());

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, doc));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		if (rDoc == null) {
			rDoc = clazz.cast(getDocument(clazz, (org.openntf.domino.Document) null));
		} else {
			((DefaultDocument) rDoc).afterCreate().setModified(false);
		}

		return rDoc;
	}

	@Override
	public org.riverframework.domino.Document getDocument(String... parameters) {
		return getDocument(null, false, parameters);
	}

	@Override
	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, String... parameters) {
		return getDocument(clazz, false, parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist,
			String... parameters)
	{
		U rDoc = null;
		org.openntf.domino.Document doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			if (id.length() == 32) {
				doc = _db.getDocumentByUNID(id);
			}

			if (doc == null && id.length() == 8) {
				doc = _db.getDocumentByID(id);
			}

			if (doc == null && clazz != null && Unique.class.isAssignableFrom(clazz)) {
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

				org.openntf.domino.View view = _db.getView(indexName);
				if (view == null)
					throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but the index view "
							+ indexName + " does not exist.");

				view.refresh();
				doc = view.getDocumentByKey(id, true);
			}

			if (doc == null && createIfDoesNotExist) {
				// Creating a new document
				if (clazz == null)
					throw new RiverException(
							"It's not possible create the document if it does not exists, if the class parameter is null.");

				rDoc = createDocument(clazz, parameters);

				// If implements "Unique", setting the Id
				if (Unique.class.isAssignableFrom(clazz)) {
					((Unique) rDoc).setId(id);
				}
			} else {
				// Returning what was found
				if (clazz == null) {
					rDoc = (U) getDocument(DefaultDocument.class, doc);
				} else {
					rDoc = clazz.cast(getDocument(clazz, doc));
				}
			}
		} else {
			// There's no parameters. Returning a closed 'clazz' object
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, null));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		return rDoc;
	}

	@Override
	public org.riverframework.domino.Document getDocument(org.openntf.domino.Document doc) {
		return getDocument(null, doc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, org.openntf.domino.Document doc) {
		U rDoc = null;
		Class<?> c = null;

		if (clazz == null) {
			// If there's no a explicit class...
			if (doc != null) {
				// we try to get it from the document
				String className = doc.getItemValueString(Document.FIELD_CLASS);
				try {
					c = Class.forName(className);
				} catch (ClassNotFoundException e) {
					c = null;
				}
			}
			// If that was not possible, we assumed the DefaultDocument.class
			if (c == null)
				c = DefaultDocument.class;

		} else {
			// If the class was explicit declared, we check if it inherit from DefaultDocument
			if (DefaultDocument.class.isAssignableFrom(clazz)) {
				c = clazz;
			} else {
				// Otherwise, we assumed the DefaultDocument.class
				c = DefaultDocument.class;
			}
		}

		try {
			Constructor<?> constructor = c.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
			constructor.setAccessible(true);
			rDoc = (U) constructor.newInstance(this, doc);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDoc;
	}

	@Override
	public org.riverframework.domino.View getView(String... parameters) {
		return getView(DefaultView.class, parameters);
	}

	@Override
	public <U extends org.riverframework.domino.View> U getView(Class<U> clazz, String... parameters) {
		U rView = null;
		org.openntf.domino.View view = null;
		String id = parameters[0];

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		view = _db.getView(id);

		if (view != null)
			view.setAutoUpdate(false);

		if (DefaultView.class.isAssignableFrom(clazz)) {
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.View.class);
				constructor.setAccessible(true);
				rView = clazz.cast(constructor.newInstance(this, view));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		return rView;
	}

	@Override
	public org.riverframework.domino.DocumentCollection getAllDocuments() {
		org.openntf.domino.DocumentCollection col = _db.getAllDocuments();
		DocumentCollection result = new DefaultDocumentCollection(this, col);

		return result;
	}

	@Override
	public org.riverframework.domino.DocumentCollection search(String query) {
		org.openntf.domino.DocumentCollection col = _db.FTSearch(query);
		DocumentCollection result = new DefaultDocumentCollection(this, col);

		return result;
	}

	@Override
	public org.riverframework.domino.Database refreshSearchIndex() {
		_db.updateFTIndex(false);
		return this;
	}

	@Override
	public org.riverframework.domino.Counter getCounter(String key) {
		Counter counter = getDocument(DefaultCounter.class, true, key);

		return counter;
	}
}
