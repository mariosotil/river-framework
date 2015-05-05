package org.riverframework.wrapper.lotus.domino;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentList;
import org.riverframework.wrapper.View;

class DefaultView implements org.riverframework.wrapper.View {
	protected org.riverframework.wrapper.Session session = null;
	protected lotus.domino.View __view = null;

	public DefaultView(org.riverframework.wrapper.Session s, lotus.domino.View v) {
		__view = v;
		session = s;
		((DefaultSession) session).registerObject(__view);
	}

	@Override
	public String getObjectId() {
		try {
			return __view.getName();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public lotus.domino.View getNativeObject() {
		return __view;
	}

	@SuppressWarnings("unused")
	@Override
	public Document getDocumentByKey(String key) {
		lotus.domino.Document _doc = null;

		try {
			_doc = __view.getDocumentByKey(key, true);
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
		return __view != null;
	}

	@Override
	public DocumentList getAllDocuments() {
		lotus.domino.ViewEntryCollection _col;
		try {
			_col = __view.getAllEntries();
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
	public DocumentList getAllDocumentsByKey(Object key) {
		lotus.domino.DocumentCollection _col;
		try {
			_col = __view.getAllDocumentsByKey(key, true);
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
	public View refresh() {
		try {
			__view.refresh();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public DocumentList search(String query) {
		try {
			__view.FTSearch(query);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		DocumentList result = new DefaultDocumentList(session, __view);

		return result;
	}

	@Override
	public void close() {
		try {
			if (__view != null)
				__view.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__view = null;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
