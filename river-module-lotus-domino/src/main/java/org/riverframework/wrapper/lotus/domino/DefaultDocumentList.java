package org.riverframework.wrapper.lotus.domino;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.DocumentList;

class DefaultDocumentList extends ArrayList<Document> implements DocumentList {
	private static final long serialVersionUID = -8572804835705647207L;
	//private enum Type { COLLECTION, VIEW, VIEW_ENTRY_COLLECTION }

//	private lotus.domino.DocumentCollection _col = null;
//	private lotus.domino.View _view = null;
//	private lotus.domino.ViewEntryCollection _vecol = null;

	private org.riverframework.wrapper.Session _session = null;
	
	//private Type type = null; 

	public DefaultDocumentList(org.riverframework.wrapper.Session s, lotus.domino.DocumentCollection _col) {
//		type = Type.COLLECTION;
		_session = s;
//		this._col = _col;
		
		DocumentIterator _iterator = new DefaultDocumentIterator(s, _col);

		for(Document _doc : _iterator) {
			this.add(_doc);
		}
	}

	public DefaultDocumentList(org.riverframework.wrapper.Session s, lotus.domino.View _view) {
//		type = Type.VIEW;
_session = s;
//		this._view = _view;

		DocumentIterator _iterator = new DefaultDocumentIterator(s, _view);

		for(Document _doc : _iterator) {
			this.add(_doc);
		}
	}

	public DefaultDocumentList(org.riverframework.wrapper.Session s, lotus.domino.ViewEntryCollection _vecol) {
//		type = Type.VIEW_ENTRY_COLLECTION;
_session = s;
//		this._vecol = _vecol;
		
		DocumentIterator _iterator = new DefaultDocumentIterator(s, _vecol);

		for(Document _doc : _iterator) {
			this.add(_doc);
		}
	}

	@Override
	public DocumentIterator iterator() {
		DocumentIterator _iterator = new DefaultDocumentIterator(_session, this);
		
//		switch(type) {
//		case COLLECTION:
//			_iterator = new DefaultDocumentIterator(_session, _col);
//			break;
//		case VIEW:
//			_iterator = new DefaultDocumentIterator(_session, _view);
//			break;
//		case VIEW_ENTRY_COLLECTION:
//			_iterator = new DefaultDocumentIterator(_session, _vecol);
//		break;
//		}
		
		return _iterator;		
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
