package org.riverframework.domino;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.riverframework.RiverException;

public class DefaultDatabase implements org.riverframework.domino.Database {
	public static final String METHOD_GETINDEXNAME = "getIndexName";
	protected Session session = null;
	protected org.openntf.domino.Database database = null;

	protected DefaultDatabase(org.riverframework.domino.Session s, org.openntf.domino.Database obj) {
		session = s;
		database = obj;
	}

	@Override
	public String getServer() {
		try {
			return database.getServer();
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getFilePath() {
		try {
			return database.getFilePath();
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getName() {
		try {
			return database.getTitle();
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return (database != null && database.isOpen());
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Database getMainReplica() {
		String adminServer = "";
		String replicaId = "";

		try {
			adminServer = database.getACL().getAdministrationServer();
			if (adminServer.equals(""))
				return this;

			replicaId = database.getReplicaID();
		} catch (Exception e) {
			throw new RiverException(e);
		}

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

		try {
			if (DefaultDocument.class.isAssignableFrom(clazz)) {
				doc = database.createDocument();
				doc.replaceItemValue(org.riverframework.Document.FIELD_CLASS, clazz.getCanonicalName());

				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, doc));
			}
		} catch (Exception e) {
			throw new RiverException(e);
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

	@Override
	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist,
			String... parameters)
	{
		U rDoc = null;
		org.openntf.domino.Document doc = null;

		try {
			if (parameters.length > 0) {
				String id = parameters[0];

				if (id.length() == 32) {
					doc = database.getDocumentByUNID(id);
				}

				if (doc == null && id.length() == 8) {
					doc = database.getDocumentByID(id);
				}

				if (doc == null && clazz != null && Unique.class.isAssignableFrom(clazz)) {
					Method method = clazz.getMethod(METHOD_GETINDEXNAME);
					if (method == null)
						throw new RiverException("The class " + clazz.getSimpleName()
								+ " implements Unique but it does not implement the method " + METHOD_GETINDEXNAME + ".");

					method.setAccessible(true);
					String indexName = (String) method.invoke(null);
					if (indexName.equals(""))
						throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but its method "
								+ METHOD_GETINDEXNAME + " returns an empty string.");

					org.openntf.domino.View view = database.getView(indexName);
					if (view == null)
						throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but the index view "
								+ indexName + " does not exist.");

					view.refresh();
					doc = view.getDocumentByKey(id, true);
				}

				if (doc == null && createIfDoesNotExist) {
					// Creating a new document
					rDoc = createDocument(clazz, parameters);

					// If implements "Unique", setting the Id
					if (Unique.class.isAssignableFrom(clazz)) {
						((Unique) rDoc).setId(id);
					}
				} else {
					// Returning what was found
					rDoc = clazz.cast(getDocument(clazz, doc));
				}
			} else {
				// There's no parameters. Returning a closed 'clazz' object
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, null));
			}

		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDoc;
	}

	@Override
	public org.riverframework.domino.Document getDocument(org.openntf.domino.Document doc) {
		return getDocument(null, doc);
	}

	@Override
	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, org.openntf.domino.Document doc) {
		U rDoc = null;

		try {
			Constructor<?> constructor = null;

			if (clazz != null && DefaultDocument.class.isAssignableFrom(clazz)) {
				constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				constructor.setAccessible(true);
			} else {
				constructor = DefaultDocument.class.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				constructor.setAccessible(true);
			}

			rDoc = clazz.cast(constructor.newInstance(this, doc));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDoc;
	}

	@Override
	public <U extends org.riverframework.View> U getView(Class<U> clazz, String... parameters) {
		U rView = null;
		org.openntf.domino.View view = null;
		String id = parameters[0];

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		try {
			view = database.getView(id);

			if (view != null)
				view.setAutoUpdate(false);

			if (DefaultView.class.isAssignableFrom(clazz)) {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.View.class);
				constructor.setAccessible(true);
				rView = clazz.cast(constructor.newInstance(this, view));
			}
		} catch (Exception e) {
			throw new RiverException(e);
		} finally {
			if (rView == null) {
				rView = clazz.cast(new DefaultView(this, null));
			}
		}

		return rView;
	}

	@Override
	public DocumentCollection getAllDocuments() {
		DocumentCollection rDocumentIterator = null;
		org.openntf.domino.DocumentCollection col = null;

		try {
			col = database.getAllDocuments();
		} catch (Exception e) {
			throw new RiverException(e);
		}

		rDocumentIterator = new DefaultDocumentCollection(this, col);

		return rDocumentIterator;
	}

	@Override
	public org.riverframework.domino.Counter getCounter(String key) {
		Counter counter = getDocument(DefaultCounter.class, true, key);

		return counter;
	}
}
