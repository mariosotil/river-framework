package org.riverframework.domino;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.riverframework.Document;
import org.riverframework.RiverException;

public class DefaultDatabase implements org.riverframework.domino.Database {
	public static final String METHOD_GETINDEXNAME = "getIndexName";
	protected Session session = null;
	// private Counter counter = null;
	protected org.openntf.domino.Database database = null;

	protected DefaultDatabase(org.riverframework.domino.Session s, org.openntf.domino.Database obj) {
		session = s;
		open(obj);
	}

	public DefaultDatabase(org.riverframework.domino.Session s, String... location) {
		session = s;
		open(location);
	}

	@Override
	public Database open(org.openntf.domino.Database obj) {
		database = obj;
		return this;
	}

	@Override
	public Database open(String... location) {
		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path; or server and replicaID");

		String server = location[0];
		String path = location[1];

		try {
			if (path.length() == 16) {
				database = session.getNotesSession().getDatabase(null, " ", true);
				database.openByReplicaID(server, path);
			}

			if (database == null || !database.isOpen()) {
				database = session.getNotesSession().getDatabase(server, path, false);
			}

			if (database != null && !database.isOpen()) {
				// database.recycle();
				database = null;
			}

		} catch (Exception e) {
			throw new RiverException(e);
		}

		return this;
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
	public <U extends org.riverframework.Document> U createDocument(Class<U> clazz, String... parameters) {
		U rDoc = null;
		org.openntf.domino.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		try {
			if (Document.class.isAssignableFrom(clazz)) {

				doc = database.createDocument();
				doc.replaceItemValue(org.riverframework.Document.FIELD_CLASS, clazz.getSimpleName());

				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, doc));
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		if (rDoc == null) {
			rDoc = clazz.cast(new DefaultDocument(this, null));
		} else {
			((DefaultDocument) rDoc).afterCreate().setModified(false);
		}

		return rDoc;
	}

	@Override
	public <U extends org.riverframework.Document> U getDocument(Class<U> clazz, String... parameters) {
		return getDocument(clazz, false, parameters);
	}

	@Override
	public <U extends org.riverframework.Document> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters)
	{
		U rDoc = null;
		org.openntf.domino.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		try {
			if (parameters.length > 0) {
				String id = parameters[0];

				if (id.length() == 32) {
					doc = database.getDocumentByUNID(id);
				}

				if (doc == null && (id).length() == 8) {
					doc = database.getDocumentByID(id);
				}

				if (doc == null && Unique.class.isAssignableFrom(clazz)) {
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
					if (DefaultDocument.class.isAssignableFrom(clazz)) {
						if (rDoc == null || !rDoc.isOpen()) {
							Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, org.openntf.domino.Document.class);
							constructor.setAccessible(true);
							rDoc = clazz.cast(constructor.newInstance(this, doc));
						}
					}
				}
			} else {
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
