package org.riverframework.wrapper.org.openntf.domino;

import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentCollection;
import org.riverframework.wrapper.View;

class DefaultView implements org.riverframework.wrapper.View {
	protected org.openntf.domino.View _view = null;

	public DefaultView(org.openntf.domino.View v) {
		_view = v;
	}

	@Override
	public Object getWrappedObject() {
		return _view;
	}

	@Override
	public Document getDocumentByKey(String key) {
		org.openntf.domino.Document _doc = _view.getDocumentByKey(key, true);
		Document doc = new DefaultDocument(_doc);
		return doc;
	}

	@Override
	public boolean isOpen() {
		return _view != null;
	}

	@Override
	public DocumentCollection getAllDocuments() {
		org.openntf.domino.ViewEntryCollection _col = _view.getAllEntries();
		DocumentCollection result = new DefaultDocumentCollection(_col);

		return result;
	}

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key) {
		org.openntf.domino.DocumentCollection _col = _view.getAllDocumentsByKey(key, true);
		DocumentCollection result = new DefaultDocumentCollection(_col);

		return result;
	}

	@Override
	public View refresh() {
		_view.refresh();
		return this;
	}

	@Override
	public DocumentCollection search(String query) {
		_view.FTSearch(query);
		DocumentCollection result = new DefaultDocumentCollection(_view);

		return result;
	}

	@Override
	public void close() {
		_view = null;
	}
}
