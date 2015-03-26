package org.riverframework.wrapper.domino;

import lotus.domino.NotesException;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentCollection;
import org.riverframework.wrapper.View;

public class DefaultView implements org.riverframework.wrapper.View {
	protected lotus.domino.View _view = null;

	public DefaultView(lotus.domino.View v) {
		_view = v;
	}

	@Override
	public Document getDocumentByKey(String key) {
		lotus.domino.Document _doc = null;

		try {
			_doc = _view.getDocumentByKey(key, true);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Document doc = new DefaultDocument(_doc);
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

		DocumentCollection result = new DefaultDocumentCollection(_col);

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
		DocumentCollection result = new DefaultDocumentCollection(_col);

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
		DocumentCollection result = new DefaultDocumentCollection(_view);

		return result;
	}
}
