package org.riverframework.wrapper.lotus.domino;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultDatabase implements org.riverframework.wrapper.Database {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected org.riverframework.wrapper.Session _session = null;
	protected lotus.domino.Database __database = null;
	private String objectId = null;

	protected DefaultDatabase(org.riverframework.wrapper.Session _s, lotus.domino.Database __obj) {
		__database = __obj;
		_session = _s;
		calcObjectId();		
	}

	@Override
	public lotus.domino.Database getNativeObject() {
		return __database;
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	private void calcObjectId() {
		if (__database != null) {
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
			return (__database != null && __database.isOpen());
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Document createDocument(String... parameters) {
		lotus.domino.Document __doc = null;

		try {
			__doc = __database.createDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		//Document doc = Factory.createDocument(session, __doc);
		Document doc = _session.getFactory().getDocument(__doc);
		return doc;
	}

	@Override
	public Document getDocument(String... parameters)
	{
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
			} catch (NotesException e) {
				// Maybe it was an invalid UNID. We just ignore the exception.
//				try {
//					XXX if (__doc != null) __doc.recycle();
//				} catch (NotesException e1) {
//					Do nothing
//				} finally {
//					__doc = null;
//				}
			}

			try {
				if (__doc == null && id.length() == 8) {
					__doc = __database.getDocumentByID(id);
				}
			} catch (NotesException e) {
				// Maybe it was an invalid UNID. We just ignore the exception.
//				try {
//					XXX if (__doc != null) __doc.recycle();
//				} catch (NotesException e1) {
//					Do nothing
//				} finally {
//					__doc = null;
//				}
			}
		}

		//Document doc = Factory.createDocument(_session, _doc);
		//Document doc = ((org.riverframework.wrapper.lotus.domino.DefaultSession) _session).getFactory().getDocument(_doc);
		Document doc = _session.getFactory().getDocument(__doc);

		return doc;
	}

	@Override
	public View getView(String... parameters) {
		lotus.domino.View _view = null;

		try {
			if (parameters.length > 0) {
				String id = parameters[0];
				_view = __database.getView(id);
			}

			if (_view != null)
				_view.setAutoUpdate(false);

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		//View view = Factory.createView(_session, _view);
		//View view = ((org.riverframework.wrapper.lotus.domino.DefaultSession) _session).getFactory().getView(_view);
		View view = _session.getFactory().getView(_view);
		return view;
	}

	@Override
	public DocumentIterator getAllDocuments() {
		lotus.domino.DocumentCollection _col;

		try {
			_col = __database.getAllDocuments();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		DocumentIterator _iterator = _session.getFactory().getDocumentIterator(_col);
		
		//		2015.05.08: I couldn't recycle this object at this time, because the Iterator 
		//		will need it. So, I believe that the better approach is to find a better way  
		//		to manage the objects to be recycled in automatic way. 
		//		try {
		//			_col.recycle();
		//		} catch (NotesException e) {
		//			throw new RiverException(e);
		//		}

		return _iterator;
	}

	@Override
	public DocumentIterator search(String query) {
		lotus.domino.DocumentCollection _col;

		try {
			_col = __database.FTSearch(query);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		DocumentIterator _iterator = _session.getFactory().getDocumentIterator(_col);

		//		2015.05.08: I couldn't recycle this object at this time, because the Iterator 
		//		will need it. So, I believe that the better approach is to find a better way  
		//		to manage the objects to be recycled in automatic way. 
		//		try {
		//			_col.recycle();
		//		} catch (NotesException e) {
		//			throw new RiverException(e);
		//		}
		return _iterator;
	}

	@Override
	public Database refreshSearchIndex() {
		try {
			__database.updateFTIndex(false);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void close() {
//		try {
//			if (__database != null)
//				__database.recycle();
//		} catch (NotesException e) {
//			throw new RiverException(e);
//		} finally {
//			__database = null;
//		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public void finalize() {
		log.finest("Finalized: id=" + objectId + " (" + this.hashCode() + ")");
	}
}
