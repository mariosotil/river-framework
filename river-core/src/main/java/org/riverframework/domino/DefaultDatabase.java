package org.riverframework.domino;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import lotus.domino.NotesException;

import org.riverframework.RiverException;

public class DefaultDatabase implements org.riverframework.domino.Database {
	public static final String METHOD_GETINDEXNAME = "getIndexName";
	protected Session session = null;
	protected lotus.domino.Database _db = null;

	protected DefaultDatabase(org.riverframework.domino.Session s, lotus.domino.Database obj) {
		session = s;
		_db = obj;
	}
	
	@Override
	public org.riverframework.domino.Session getSession() {
		return session;
	}

	@Override
	public String getServer() {
		try {
			return _db.getServer();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getFilePath() {
		try {
			return _db.getFilePath();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getName() {
		try {
			return _db.getTitle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return (_db != null && _db.isOpen());
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Database getMainReplica() {
		throw new UnsupportedOperationException();

//		2015.03.25 - This feature does not work in the version 4 of Openntf Domino API
//		String adminServer = "";
//		String replicaId = "";
//
//		adminServer = _db.getACL().getAdministrationServer();
//		if (adminServer.equals(""))
//			return this;
//
//		replicaId = _db.getReplicaID();
//
//		Database d = session.getDatabase(DefaultDatabase.class, adminServer, replicaId);
//
//		return d;
	}

	@Override
	public org.riverframework.domino.Document createDocument(String... parameters) {
		return createDocument(DefaultDocument.class, parameters);
	}

	@Override
	public <U extends org.riverframework.domino.Document> U createDocument(Class<U> clazz, String... parameters) {
		U rDoc = null;
		lotus.domino.Document doc = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (DefaultDocument.class.isAssignableFrom(clazz)) {
			try {
				doc = _db.createDocument();
				doc.replaceItemValue(org.riverframework.Document.FIELD_CLASS, clazz.getCanonicalName());
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, lotus.domino.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, doc));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		if (rDoc == null) {
			rDoc = clazz.cast(getDocument(clazz, (lotus.domino.Document) null));
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
		lotus.domino.Document doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			if (id.length() == 32) {
				try {
					doc = _db.getDocumentByUNID(id);
				} catch (NotesException e) {
					throw new RiverException(e);
				}
			}

			if (doc == null && id.length() == 8) {
				try {
					doc = _db.getDocumentByID(id);
				} catch (NotesException e) {
					throw new RiverException(e);
				}
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

				lotus.domino.View view;
				try {
					view = _db.getView(indexName);
				} catch (NotesException e) {
					throw new RiverException(e);
				}

				if (view == null)
					throw new RiverException("The class " + clazz.getSimpleName() + " implements Unique but the index view "
							+ indexName + " does not exist.");

				try {
					view.refresh(); //TODO: this is EXPENSIVE!
					doc = view.getDocumentByKey(id, true);
				} catch (NotesException e) {
					throw new RiverException(e);
				}
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
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, lotus.domino.Document.class);
				constructor.setAccessible(true);
				rDoc = clazz.cast(constructor.newInstance(this, null));
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		return rDoc;
	}

	@Override
	public org.riverframework.domino.Document getDocument(lotus.domino.Document doc) {
		return getDocument(null, doc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.domino.Document> U getDocument(Class<U> clazz, lotus.domino.Document doc) {
		U rDoc = null;
		Class<?> c = null;

		if (clazz == null) {
			// If there's no a explicit class...
			if (doc != null) {
				// we try to get it from the document
				String className;
				try {
					className = doc.getItemValueString(Document.FIELD_CLASS);
				} catch (NotesException e) {
					throw new RiverException(e);
				}
				
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
			Constructor<?> constructor = c.getDeclaredConstructor(Database.class, lotus.domino.Document.class);
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
		lotus.domino.View view = null;
		String id = parameters[0];

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		try {
			view = _db.getView(id);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		if (view != null)
			try {
				view.setAutoUpdate(false);
			} catch (NotesException e) {
				throw new RiverException(e);
			}

		if (DefaultView.class.isAssignableFrom(clazz)) {
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Database.class, lotus.domino.View.class);
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
		lotus.domino.DocumentCollection _col;
		try {
			_col = _db.getAllDocuments();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		DocumentCollection result = new DefaultDocumentCollection(this).loadFrom(_col);

		return result;
	}

	@Override
	public org.riverframework.domino.DocumentCollection search(String query) {
		lotus.domino.DocumentCollection _col;
		try {
			_col = _db.FTSearch(query);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		DocumentCollection result = new DefaultDocumentCollection(this).loadFrom(_col);

		return result;
	}

	@Override
	public org.riverframework.domino.Database refreshSearchIndex() {
		try {
			_db.updateFTIndex(false);
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public org.riverframework.domino.Counter getCounter(String key) {
		Counter counter = getDocument(DefaultCounter.class, true, key);

		return counter;
	}
}
