package org.riverframework.wrapper.lotus.domino;

import java.util.ArrayList;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentList;

class DefaultDocumentList extends ArrayList<Document> implements DocumentList {
	private static final long serialVersionUID = -8572804835705647207L;

	public DefaultDocumentList(org.riverframework.wrapper.Session s, lotus.domino.DocumentCollection _col) {
		lotus.domino.Document __doc = null;

		clear();
		try {
			__doc = _col.getFirstDocument();
			while (__doc != null) {
				Document doc = new DefaultDocument(s, __doc);
				this.add(doc);
				__doc = _col.getNextDocument(__doc);
			}
		} catch (NotesException e) {
			throw new RiverException();
		}
	}

	public DefaultDocumentList(org.riverframework.wrapper.Session s, lotus.domino.View _view) {
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

	public DefaultDocumentList(org.riverframework.wrapper.Session s, lotus.domino.ViewEntryCollection _col) {
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
	public DocumentList deleteAll() {
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
