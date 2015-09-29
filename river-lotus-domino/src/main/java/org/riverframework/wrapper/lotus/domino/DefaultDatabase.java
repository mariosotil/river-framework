package org.riverframework.wrapper.lotus.domino;

import lotus.domino.Base;
import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultDatabase extends AbstractBaseLotusDomino<lotus.domino.Database> implements org.riverframework.wrapper.Database<lotus.domino.Database> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected DefaultDatabase(org.riverframework.wrapper.Session<lotus.domino.Session> _session, lotus.domino.Database __native) {
		super(_session, __native);
	}

	@Override
	public boolean isRecycled() {
		// If it's not a remote session, returns false. Otherwise, returns if the object is recycled
		return isObjectRecycled(__native);
	}
	
	public String calcObjectId(lotus.domino.Database __database) {
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
			return __native.getServer();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getFilePath() {
		try {
			return __native.getFilePath();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getName() {
		try {
			String name = __native.getTitle();
			return name.equals("") ? __native.getFileName() : name;
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return (__native != null && !isRecycled() && __native.isOpen()); 
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Document<lotus.domino.Document> createDocument(String... parameters) {
		lotus.domino.Document __doc = null;

		try {
			__doc = __native.createDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		Document<lotus.domino.Document> doc = (Document<lotus.domino.Document>) _factory.getDocument(__doc);

		return doc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<lotus.domino.Document> getDocument(String... parameters)
	{
		lotus.domino.Document __doc = null;
		Document<lotus.domino.Document> doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			doc = (Document<lotus.domino.Document>) _factory.getDocument(id);

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
				
				doc = (Document<lotus.domino.Document>) _factory.getDocument(__doc);
			}
		} else {
			doc = (Document<lotus.domino.Document>) _factory.getDocument((lotus.domino.Document) null);
		}

		return doc;
	}

	@Override
	public View<lotus.domino.View> createView(String... parameters) {
		lotus.domino.View __view = null;
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
		} catch (NotesException e) {
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

		View<lotus.domino.View> _view = getView(name);
		return _view;
	}

	@Override
	public View<lotus.domino.View> getView(String... parameters) {
		lotus.domino.View __view = null;

		try {
			if (parameters.length > 0) {
				String id = parameters[0];
				__view = __native.getView(id);
			}

			if (__view != null)
				__view.setAutoUpdate(false);

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		View<lotus.domino.View> _view = (View<lotus.domino.View>) _factory.getView(__view);
		return _view;
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> getAllDocuments() {
		lotus.domino.DocumentCollection _col;

		try {
			_col = __native.getAllDocuments();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = 
				(DocumentIterator<Base, lotus.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> search(String query) {
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = 
				search(query, 0);
		return _iterator;
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> search(String query, int max) {
		lotus.domino.DocumentCollection _col;

		try {
			_col = __native.search(query, null, max);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = 
				(DocumentIterator<Base, lotus.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public Database<lotus.domino.Database> refreshSearchIndex(boolean createIfNotExist) {
		try {
			__native.updateFTIndex(createIfNotExist);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void delete() {
		try {
			if (__native != null) 
				__native.remove();			
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
