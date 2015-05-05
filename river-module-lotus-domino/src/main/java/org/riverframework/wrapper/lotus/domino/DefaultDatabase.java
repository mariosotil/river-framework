package org.riverframework.wrapper.lotus.domino;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentList;
import org.riverframework.wrapper.View;

class DefaultDatabase implements org.riverframework.wrapper.Database {
	protected org.riverframework.wrapper.Session session = null;
	protected lotus.domino.Database __database = null;

	protected DefaultDatabase(org.riverframework.wrapper.Session s, lotus.domino.Database obj) {
		__database = obj;
		session = s;
		((DefaultSession) session).registerObject(__database);
	}

	@Override
	public lotus.domino.Database getNativeObject() {
		return __database;
	}

	@Override
	public String getObjectId() {
		try {
			return __database.getReplicaID();
		} catch (NotesException e) {
			throw new RiverException(e);
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
		lotus.domino.Document _doc = null;

		try {
			_doc = __database.createDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Document doc = new DefaultDocument(session, _doc);
		return doc;
	}

	@SuppressWarnings("unused")
	@Override
	public Document getDocument(String... parameters)
	{
		lotus.domino.Document _doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			try {
				if (id.length() == 32) {
					_doc = __database.getDocumentByUNID(id);
				}

				if (_doc == null && id.length() == 8) {
					_doc = __database.getDocumentByID(id);
				}
			} catch (NotesException e) {
				try {
					if (_doc != null)
						_doc.recycle();
				} catch (NotesException e1) {
				} finally {
					_doc = null;
				}

				throw new RiverException(e);
			}
		}

		Document doc = new DefaultDocument(session, _doc);
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

		View view = new DefaultView(session, _view);
		return view;
	}

	@Override
	public DocumentList getAllDocuments() {
		lotus.domino.DocumentCollection _col;

		try {
			_col = __database.getAllDocuments();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		DocumentList col = new DefaultDocumentList(session, _col);

		try {
			_col.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return col;
	}

	@Override
	public DocumentList search(String query) {
		lotus.domino.DocumentCollection _col;

		try {
			_col = __database.FTSearch(query);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		DocumentList result = new DefaultDocumentList(session, _col);

		try {
			_col.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
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
		try {
			if (__database != null)
				__database.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__database = null;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
