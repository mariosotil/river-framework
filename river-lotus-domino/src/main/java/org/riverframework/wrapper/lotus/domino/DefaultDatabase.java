package org.riverframework.wrapper.lotus.domino;

import lotus.domino.Base;
import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.View;

class DefaultDatabase extends AbstractBase<lotus.domino.Database> implements org.riverframework.wrapper.Database<lotus.domino.Database> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected org.riverframework.wrapper.Session<lotus.domino.Session> _session = null;
	protected org.riverframework.wrapper.Factory<lotus.domino.Base> _factory = null;
	protected volatile lotus.domino.Database __database = null;
	private String objectId = null;

	@SuppressWarnings("unchecked")
	protected DefaultDatabase(org.riverframework.wrapper.Session<lotus.domino.Session> _s, lotus.domino.Database __obj) {
		__database = __obj;
		_session = _s;
		_factory = (Factory<Base>) _s.getFactory();
		// synchronized (_session){
		objectId = calcObjectId(__database);
		// }
	}

	@Override
	public boolean isRecycled() {
		// If it's not a remote session, returns false. Otherwise, returns if the object is recycled
		if (_factory.getIsRemoteSession()) {
			java.lang.reflect.Field deleted;
			try {
				deleted = lotus.domino.cso.Database.class.getDeclaredField("deleted");
				deleted.setAccessible(true);
				return deleted.getBoolean(__database);
			} catch (Exception e) {
				throw new RiverException(e);
			}
		} else {
			return isObjectRecycled(__database);
		}
	}
	
	@Override
	public lotus.domino.Database getNativeObject() {
		return __database;
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	public static String calcObjectId(lotus.domino.Database __database) {
		String objectId = "";

		if (__database != null ) { // && !isRecycled(__database)) {
			try {
				StringBuilder sb = new StringBuilder(1024);
				sb.append(__database.getServer());
				sb.append(River.ID_SEPARATOR);
				sb.append(__database.getFilePath());

				objectId = sb.toString();
			} catch (NotesException e) {
				throw new RiverException(e);
			}
		} 

		return objectId;
	}

	@Override
	public String getServer() {
		try {
			return __database.getServer();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getFilePath() {
		try {
			return __database.getFilePath();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getName() {
		try {
			return __database.getTitle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return (__database != null && !isRecycled() && __database.isOpen()); 
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Document<lotus.domino.Document> createDocument(String... parameters) {
		// synchronized (_session){
		lotus.domino.Document __doc = null;

		try {
			__doc = __database.createDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		Document<lotus.domino.Document> doc = (Document<lotus.domino.Document>) _factory.getDocument(__doc);

		return doc;
		// }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<lotus.domino.Document> getDocument(String... parameters)
	{
		// synchronized (_session){
		lotus.domino.Document __doc = null;
		Document<lotus.domino.Document> doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			doc = (Document<lotus.domino.Document>) _factory.getDocument(id);

			if (!doc.isOpen()) { 
				//			String[] temp = id.split(Pattern.quote(River.ID_SEPARATOR));
				//			if (temp.length == 3) {
				//				id = temp[2];
				//			}

				try {
					if (id.length() == 32) {
						__doc = __database.getDocumentByUNID(id);
					}
				} catch (Exception e) {
					// Do nothing
				}

				try {
					if (__doc == null && id.length() == 8) {
						__doc = __database.getDocumentByID(id);
					}
				} catch (Exception e) {
					// Do nothing
				}
				
				doc = (Document<lotus.domino.Document>) _factory.getDocument(__doc);
			}
		} else {
			doc = (Document<lotus.domino.Document>) _factory.getDocument((lotus.domino.Document) null);
		}

		return doc;
		// }
	}

	@Override
	public View<lotus.domino.View> createView(String... parameters) {
		lotus.domino.View __view = null;
		String name = null;

		try {
			if (parameters.length == 1) {
				name = parameters[0];
				__view = __database.createView(name);
			} else if (parameters.length == 2) {
				name = parameters[0];
				String selectionFormula = parameters[1];
				__view = __database.createView(name, selectionFormula);
			} else {
				throw new RiverException("It was expected these parameters: name (required) and selection formula (optional).");
			}			
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		if (name != null && !name.equals("") && __view != null) {
			try {
				// __view.recycle(); <== Never do that! Let the server do it
				__view = null;
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		View<lotus.domino.View> _view = getView(name);
		return _view;
	}

	@Override
	public View<lotus.domino.View> getView(String... parameters) {
		// synchronized (_session){
		lotus.domino.View __view = null;

		try {
			if (parameters.length > 0) {
				String id = parameters[0];
				__view = __database.getView(id);
			}

			if (__view != null)
				__view.setAutoUpdate(false);

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		View<lotus.domino.View> _view = (View<lotus.domino.View>) _factory.getView(__view);
		return _view;
		// }
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> getAllDocuments() {
		// synchronized (_session){
		lotus.domino.DocumentCollection _col;

		try {
			_col = __database.getAllDocuments();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = 
				(DocumentIterator<Base, lotus.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
		// }
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> search(String query) {
		// synchronized (_session){
		lotus.domino.DocumentCollection _col;

		try {
			_col = __database.FTSearch(query);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = 
				(DocumentIterator<Base, lotus.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
		// }
	}

	@Override
	public Database<lotus.domino.Database> refreshSearchIndex(boolean createIfNotExist) {
		try {
			__database.updateFTIndex(createIfNotExist);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void delete() {
		try {
			if (__database != null) 
				__database.remove();			
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		close();
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}

	@Override
	@Deprecated
	public void close() {
		// Don't recycle or close it. Let the server do that.
	}
}
