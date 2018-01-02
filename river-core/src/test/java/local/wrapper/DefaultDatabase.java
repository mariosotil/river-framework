package local.wrapper;

import local.mock.Base;
import local.mock.DatabaseException;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultDatabase extends AbstractBaseNoSQL<local.mock.Database> implements org.riverframework.wrapper.Database<local.mock.Database> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected DefaultDatabase(org.riverframework.wrapper.Session<local.mock.Session> _session, local.mock.Database __native) {
		super(_session, __native);
	}

	@Override
	public boolean isRecycled() {
		// If it's not a remote session, returns false. Otherwise, returns if the object is recycled
		return AbstractBaseNoSQL.isObjectRecycled(__native);
	}
	
	public String calcObjectId(local.mock.Database __database) {
		String objectId = "";

		if (__database != null ) { // && !isRecycled(__database)) {
			try {
				StringBuilder sb = new StringBuilder(1024);
				sb.append(__database.getServer());
				sb.append(River.ID_SEPARATOR);
				sb.append(__database.getFilePath());

				objectId = sb.toString();
			} catch (DatabaseException e) {
				throw new RiverException(e);
			}
		} 

		return objectId;
	}

	@Override
	public String getServer() {
		try {
			return __native.getServer();
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getFilePath() {
		try {
			return __native.getFilePath();
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getName() {
		try {
			String name = __native.getTitle();
			return name.equals("") ? __native.getFileName() : name;
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return (__native != null && !isRecycled() && __native.isOpen()); 
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Document<local.mock.Document> createDocument(String... parameters) {
		local.mock.Document __doc = null;

		try {
			__doc = __native.createDocument();
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		Document<local.mock.Document> doc = (Document<local.mock.Document>) _factory.getDocument(__doc);

		return doc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<local.mock.Document> getDocument(String... parameters)
	{
		local.mock.Document __doc = null;
		Document<local.mock.Document> doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			doc = (Document<local.mock.Document>) _factory.getDocument(id);

			if (!doc.isOpen()) { 
				try {
					if (id.length() == 32) {
						__doc = __native.getDocumentByUNID(id);
					}
				} catch (Exception e) {
					// Do nothing
				}

				try {
					if (__doc == null && id.length() == 8) {
						__doc = __native.getDocumentByID(id);
					}
				} catch (Exception e) {
					// Do nothing
				}
				
				doc = (Document<local.mock.Document>) _factory.getDocument(__doc);
			}
		} else {
			doc = (Document<local.mock.Document>) _factory.getDocument((local.mock.Document) null);
		}

		return doc;
	}

	@Override
	public View<local.mock.View> createView(String... parameters) {
		local.mock.View __view = null;
		String name = null;

		try {
			if (parameters.length == 1) {
				name = parameters[0];
				__view = __native.createView(name);
			} else if (parameters.length == 2) {
				name = parameters[0];
				String selectionFormula = parameters[1];
				__view = __native.createView(name, selectionFormula);
			} else {
				throw new RiverException("It was expected these parameters: name (required) and selection formula (optional).");
			}			
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		if (name != null && !name.equals("") && __view != null) {
			try {
				if (_factory.getIsRemoteSession()) 
					__view.recycle();
				
				__view = null;
			} catch (Exception e) {
				throw new RiverException(e);
			}
		}

		View<local.mock.View> _view = getView(name);
		return _view;
	}

	@Override
	public View<local.mock.View> getView(String... parameters) {
		local.mock.View __view = null;

		try {
			if (parameters.length > 0) {
				String id = parameters[0];
				__view = __native.getView(id);
			}

			if (__view != null)
				__view.setAutoUpdate(false);

		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		View<local.mock.View> _view = (View<local.mock.View>) _factory.getView(__view);
		return _view;
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> getAllDocuments() {
		local.mock.DocumentCollection _col;

		try {
			_col = __native.getAllDocuments();
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<local.mock.Base, local.mock.Document> _iterator =
				(DocumentIterator<Base, local.mock.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> search(String query) {
		DocumentIterator<local.mock.Base, local.mock.Document> _iterator =
				search(query, 0);
		return _iterator;
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> search(String query, int max) {
		local.mock.DocumentCollection _col;

		try {
			_col = __native.search(query, null, max);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<local.mock.Base, local.mock.Document> _iterator =
				(DocumentIterator<Base, local.mock.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public Database<local.mock.Database> refreshSearchIndex(boolean createIfNotExist) {
		try {
			__native.updateFTIndex(createIfNotExist);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void delete() {
		try {
			if (__native != null) 
				__native.remove();			
		} catch (DatabaseException e) {
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
