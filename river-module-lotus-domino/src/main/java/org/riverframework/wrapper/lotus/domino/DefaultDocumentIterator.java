package org.riverframework.wrapper.lotus.domino;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.DocumentList;

class DefaultDocumentIterator implements DocumentIterator {
	private enum Type { COLLECTION, VIEW_ENTRY_COLLECTION, DOCUMENT_LIST }

	private org.riverframework.wrapper.Session session = null;

	private lotus.domino.DocumentCollection _col = null;
	private lotus.domino.ViewEntryCollection _vecol = null;
	private DocumentList list = null;

	private lotus.domino.Document _doc = null;
	private lotus.domino.ViewEntry _ve = null;
	private int listIndex = -1;

	private Type type = null; 

	public DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.DocumentCollection _col) {
		session = s;
		type = Type.COLLECTION;
		this._col = _col;
		try {
			this._doc = this._col.getFirstDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	public DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.View _view) {
		session = s;
		type = Type.VIEW_ENTRY_COLLECTION;
		try {
			this._vecol = _view.getAllEntries();
			this._ve = this._vecol.getFirstEntry();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	public DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.ViewEntryCollection _vecol) {
		session = s;
		type = Type.VIEW_ENTRY_COLLECTION;
		this._vecol = _vecol;
		try {
			this._ve = this._vecol.getFirstEntry();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	public DefaultDocumentIterator(org.riverframework.wrapper.Session s, DocumentList list) {
		session = s;
		type = Type.DOCUMENT_LIST;
		this.list = list;

		listIndex = -1;
	}

	@Override
	public boolean hasNext() {
		switch(type) {
		case COLLECTION:
			return _doc != null;
		case VIEW_ENTRY_COLLECTION:
			return _ve != null;
		case DOCUMENT_LIST:
			return listIndex < list.size() - 1;
		}
		
		
		return false;
	}

	@Override
	public Document next() {
		lotus.domino.Document _current = null;
		Document doc = null;
		
		try {
			switch(type) {
			case COLLECTION:
				_current = _doc;
				_doc = _col.getNextDocument(_doc);
				doc = new DefaultDocument(session, _current);
				break;
			case VIEW_ENTRY_COLLECTION:
				_current = _ve.getDocument();
				_ve = _vecol.getNextEntry(_ve);
				doc = new DefaultDocument(session, _current);
				break;
			case DOCUMENT_LIST:
				doc = list.get(++listIndex);
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return doc;
	}

	@Override
	public DocumentIterator iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
