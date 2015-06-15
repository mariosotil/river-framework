package org.riverframework.wrapper.lotus.domino;

// import java.util.logging.Level;
// import java.util.logging.Logger;
import java.util.regex.Pattern;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultDatabase extends DefaultBase implements org.riverframework.wrapper.Database<lotus.domino.Base> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected org.riverframework.wrapper.Session<?> _session = null;
	protected volatile lotus.domino.Database __database = null;
	private String objectId = null;

	protected DefaultDatabase(org.riverframework.wrapper.Session<?> _s, lotus.domino.Database __obj) {
		__database = __obj;
		_session = _s;
		synchronized (_session){
			objectId = calcObjectId(__database);
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

		if (__database != null && !isRecycled(__database)) {
			try {
				StringBuilder sb = new StringBuilder();
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
			return (__database != null && !isRecycled(__database) && __database.isOpen());
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Document<lotus.domino.Base> createDocument(String... parameters) {
		synchronized (_session){
			lotus.domino.Document __doc = null;

			try {
				__doc = __database.createDocument();
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			@SuppressWarnings("unchecked")
			Document<lotus.domino.Base> doc = _session.getFactory().getDocument(__doc);

			return doc;
		}
	}

	@Override
	public Document<lotus.domino.Base> getDocument(String... parameters)
	{
		synchronized (_session){
			lotus.domino.Document __doc = null;

			if (parameters.length > 0) {
				String id = parameters[0];

				String[] temp = id.split(Pattern.quote(River.ID_SEPARATOR));
				if (temp.length == 3) {
					id = temp[2];
				}

				try {
					if (id.length() == 32) {
						__doc = __database.getDocumentByUNID(id);
					}
				} catch (Exception e) {
					// Maybe it was an invalid UNID. We just ignore the exception.
//					try {
//						if (__doc != null) __doc.recycle();
//					} catch (NotesException e1) {
//						log.log(Level.WARNING, "Exception while getting the object " + id, e);
//
//					} finally {
//						__doc = null;
//					}
				}

				try {
					if (__doc == null && id.length() == 8) {
						__doc = __database.getDocumentByID(id);
					}
				} catch (Exception e) {
					// Maybe it was an invalid UNID. We just ignore the exception.
//					try {
//						 if (__doc != null) __doc.recycle();
//					} catch (NotesException e1) {
//						 log.log(Level.WARNING, "Exception while getting the object " + id, e);
//
//					} finally {
//						__doc = null;
//					}
				}
			}

			@SuppressWarnings("unchecked")
			Document<lotus.domino.Base> doc = _session.getFactory().getDocument(__doc);

			return doc;
		}
	}

	@Override
	public View<lotus.domino.Base> createView(String... parameters) {
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

		View<lotus.domino.Base> _view = getView(name);
		return _view;
	}

	@Override
	public View<lotus.domino.Base> getView(String... parameters) {
		synchronized (_session){
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
			View<lotus.domino.Base> _view = _session.getFactory().getView(__view);
			return _view;
		}
	}

	@Override
	public DocumentIterator<lotus.domino.Base> getAllDocuments() {
		synchronized (_session){
			lotus.domino.DocumentCollection _col;

			try {
				_col = __database.getAllDocuments();
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			@SuppressWarnings("unchecked")
			DocumentIterator<lotus.domino.Base> _iterator = _session.getFactory().getDocumentIterator(_col);
			return _iterator;
		}
	}

	@Override
	public DocumentIterator<lotus.domino.Base> search(String query) {
		synchronized (_session){
			lotus.domino.DocumentCollection _col;

			try {
				_col = __database.FTSearch(query);
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			@SuppressWarnings("unchecked")
			DocumentIterator<lotus.domino.Base> _iterator = _session.getFactory().getDocumentIterator(_col);
			return _iterator;
		}
	}

	@Override
	public Database<lotus.domino.Base> refreshSearchIndex() {
		try {
			__database.updateFTIndex(false);
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
	public void close() {
		// Don't recycle or close it. Let the server do that.
//		log.finest("Closing: id=" + objectId + " (" + this.hashCode() + ")");
//
//		try {
//			if (__database != null) 
//				__database.recycle();			
//		} catch (Exception e) {
//			throw new RiverException(e);
//		} finally {
//			__database = null;
//		}
	}
}
