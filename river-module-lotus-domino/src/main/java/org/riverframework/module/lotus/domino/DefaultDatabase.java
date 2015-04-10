package org.riverframework.module.lotus.domino;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.module.Database;
import org.riverframework.module.Document;
import org.riverframework.module.DocumentList;
import org.riverframework.module.View;

class DefaultDatabase implements org.riverframework.module.Database {
	protected org.riverframework.module.Session session = null;
	protected lotus.domino.Database _database = null;

	protected DefaultDatabase(org.riverframework.module.Session s, lotus.domino.Database obj) {
		_database = obj;
		session = s;
		((DefaultSession) session).registerObject(_database);
	}

	@Override
	public Object getReferencedObject() {
		return _database;
	}

	@Override
	public String getObjectId() {
		try {
			return _database.getReplicaID();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getServer() {
		try {
			return _database.getServer();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getFilePath() {
		try {
			return _database.getFilePath();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getName() {
		try {
			return _database.getTitle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return (_database != null && _database.isOpen());
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public Document createDocument(String... parameters) {
		lotus.domino.Document _doc = null;

		try {
			_doc = _database.createDocument();
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
					_doc = _database.getDocumentByUNID(id);
				}

				if (_doc == null && id.length() == 8) {
					_doc = _database.getDocumentByID(id);
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
				_view = _database.getView(id);
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
			_col = _database.getAllDocuments();
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
			_col = _database.FTSearch(query);
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
			_database.updateFTIndex(false);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			if (_database != null)
				_database.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			_database = null;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
