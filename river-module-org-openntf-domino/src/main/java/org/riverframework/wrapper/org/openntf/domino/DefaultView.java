package org.riverframework.wrapper.org.openntf.domino;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentList;
import org.riverframework.wrapper.View;

class DefaultView implements org.riverframework.wrapper.View {
	protected org.openntf.domino.View _view = null;

	public DefaultView(org.openntf.domino.View v) {
		_view = v;
	}

	@Override
	public String getObjectId() {
		return _view.getName();
	}

	@Override
	public org.openntf.domino.View getNativeObject() {
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
	public DocumentList getAllDocuments() {
		org.openntf.domino.ViewEntryCollection _col = _view.getAllEntries();
		DocumentList result = new DefaultDocumentList(_col);

		return result;
	}

	@Override
	public DocumentList getAllDocumentsByKey(Object key) {
		org.openntf.domino.DocumentCollection _col = _view.getAllDocumentsByKey(key, true);
		DocumentList result = new DefaultDocumentList(_col);

		return result;
	}

	@Override
	public View refresh() {
		_view.refresh();
		return this;
	}

	@Override
	public DocumentList search(String query) {
		_view.FTSearch(query);
		DocumentList result = new DefaultDocumentList(_view);

		return result;
	}

	@Override
	public void close() {
		_view = null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
