package org.riverframework.wrapper.openntf;

import java.util.ArrayList;

import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentCollection;

public class DefaultDocumentCollection extends ArrayList<Document> implements DocumentCollection {
	private static final long serialVersionUID = 5182350124794069975L;

	@SuppressWarnings("deprecation")
	public DefaultDocumentCollection(org.openntf.domino.DocumentCollection _col) {
		org.openntf.domino.Document _doc = null;

		clear();
		_doc = _col.getFirstDocument();
		while (_doc != null) {
			Document doc = new DefaultDocument(_doc);
			this.add(doc);
			_doc = _col.getNextDocument(_doc);
		}
	}

	public DefaultDocumentCollection(org.openntf.domino.View _view) {
		org.openntf.domino.Document _doc = null;

		clear();
		_doc = _view.getFirstDocument();
		while (_doc != null) {
			DefaultDocument doc = new DefaultDocument(_doc);
			this.add(doc);
			_doc = _view.getNextDocument(_doc);
		}
	}

	@SuppressWarnings("deprecation")
	public DefaultDocumentCollection(org.openntf.domino.ViewEntryCollection _col) {
		org.openntf.domino.ViewEntry _entry = null;

		clear();
		_entry = _col.getFirstEntry();
		while (_entry != null) {
			DefaultDocument doc = new DefaultDocument(_entry.getDocument());
			this.add(doc);
			_entry = _col.getNextEntry(_entry);
		}
	}

	@Override
	public DocumentCollection deleteAll() {
		for (Document doc : this) {
			doc.delete();
		}
		return this;
	}

}
