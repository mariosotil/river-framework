package org.riverframework.module.lotus.domino;

import java.util.ArrayList;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.module.Document;
import org.riverframework.module.DocumentCollection;

class DefaultDocumentCollection extends ArrayList<Document> implements DocumentCollection {
	private static final long serialVersionUID = -8572804835705647207L;

	public DefaultDocumentCollection(org.riverframework.module.Session s, lotus.domino.DocumentCollection _col) {
		lotus.domino.Document _doc = null;

		clear();
		try {
			_doc = _col.getFirstDocument();
			while (_doc != null) {
				Document doc = new DefaultDocument(s, _doc);
				this.add(doc);
				_doc = _col.getNextDocument(_doc);
			}
		} catch (NotesException e) {
			throw new RiverException();
		}
	}

	public DefaultDocumentCollection(org.riverframework.module.Session s, lotus.domino.View _view) {
		lotus.domino.Document _doc = null;

		clear();
		try {
			_doc = _view.getFirstDocument();
			while (_doc != null) {
				DefaultDocument doc = new DefaultDocument(s, _doc);
				this.add(doc);
				_doc = _view.getNextDocument(_doc);
			}
		} catch (NotesException e) {
			throw new RiverException();
		}
	}

	public DefaultDocumentCollection(org.riverframework.module.Session s, lotus.domino.ViewEntryCollection _col) {
		lotus.domino.ViewEntry _entry = null;

		clear();
		try {
			_entry = _col.getFirstEntry();
			while (_entry != null) {
				DefaultDocument doc = new DefaultDocument(s, _entry.getDocument());
				this.add(doc);
				_entry = _col.getNextEntry(_entry);
			}
		} catch (NotesException e) {
			throw new RiverException();
		}
	}

	@Override
	public DocumentCollection deleteAll() {
		for (Document doc : this) {
			doc.delete();
		}
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
