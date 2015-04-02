package org.riverframework.module.lotus.domino;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.module.Document;
import org.riverframework.module.DocumentCollection;
import org.riverframework.module.View;

class DefaultView implements org.riverframework.module.View {
	protected org.riverframework.module.Session session = null;
	protected lotus.domino.View _view = null;

	public DefaultView(org.riverframework.module.Session s, lotus.domino.View v) {
		_view = v;
		session = s;
		((DefaultSession) session).registerObject(_view);
	}

	@Override
	public Object getReferencedObject() {
		return _view;
	}

	@SuppressWarnings("unused")
	@Override
	public Document getDocumentByKey(String key) {
		lotus.domino.Document _doc = null;

		try {
			_doc = _view.getDocumentByKey(key, true);
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

		Document doc = new DefaultDocument(session, _doc);
		return doc;
	}

	@Override
	public boolean isOpen() {
		return _view != null;
	}

	@Override
	public DocumentCollection getAllDocuments() {
		lotus.domino.ViewEntryCollection _col;
		try {
			_col = _view.getAllEntries();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		DocumentCollection result = new DefaultDocumentCollection(session, _col);

		try {
			_col.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key) {
		lotus.domino.DocumentCollection _col;
		try {
			_col = _view.getAllDocumentsByKey(key, true);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		DocumentCollection result = new DefaultDocumentCollection(session, _col);

		try {
			_col.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return result;
	}

	@Override
	public View refresh() {
		try {
			_view.refresh();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public DocumentCollection search(String query) {
		try {
			_view.FTSearch(query);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		DocumentCollection result = new DefaultDocumentCollection(session, _view);

		return result;
	}

	@Override
	public void close() {
		try {
			if (_view != null)
				_view.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			_view = null;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
