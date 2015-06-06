package org.riverframework.wrapper.lotus.domino;

import java.util.UUID;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.DocumentList;

class DefaultDocumentIterator implements DocumentIterator {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private enum Type { COLLECTION, VIEW_ENTRY_COLLECTION, DOCUMENT_LIST }

	private org.riverframework.wrapper.Session _session = null;

	private lotus.domino.DocumentCollection __col = null;
	private lotus.domino.ViewEntryCollection __vecol = null;
	private DocumentList _list = null;

	private lotus.domino.Document __doc = null;
	private lotus.domino.ViewEntry __ve = null;
	private int listIndex = -1;

	private Type type = null;	
	private String objectId = null;

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.DocumentCollection __obj) {
		type = Type.COLLECTION;
		_session = s;
		__col = __obj;

		try {
			this.__doc = this.__col.getFirstDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		calcObjectId();
	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.View __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;
		_session = s;		

		try {
			__vecol = __obj.getAllEntries();
			__ve = this.__vecol.getFirstEntry();
			while (__ve != null && __ve.getDocument() == null) {
				lotus.domino.ViewEntry __old = __ve;
				__ve = __vecol.getNextEntry(__ve);
				__old.recycle();
			} 
			
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		calcObjectId();
	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.ViewEntryCollection __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;		
		_session = s;		
		__vecol = __obj;

		try {
			__ve = this.__vecol.getFirstEntry();
			while (__ve != null && __ve.getDocument() == null) {
				lotus.domino.ViewEntry __old = __ve;
				__ve = __vecol.getNextEntry(__ve);
				__old.recycle();
			} 

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		calcObjectId();
	}

	public DefaultDocumentIterator(org.riverframework.wrapper.Session s, DocumentList list) {
		_session = s;
		type = Type.DOCUMENT_LIST;
		this._list = list;

		listIndex = -1;

		calcObjectId();
	}

	private void calcObjectId() {		
		switch(type) {
		case COLLECTION:
			objectId = String.valueOf(__col.hashCode());
			break;
		case VIEW_ENTRY_COLLECTION:
			objectId = String.valueOf(__vecol.hashCode());
			break;
		case DOCUMENT_LIST:
			objectId = "DocumentList";
			break;
		default:
			return;
		}

		objectId += River.ID_SEPARATOR + UUID.randomUUID().toString();
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	@Override
	public boolean hasNext() {
		switch(type) {
		case COLLECTION:
			return __doc != null;
		case VIEW_ENTRY_COLLECTION:
			return __ve != null;
		case DOCUMENT_LIST:
			return listIndex < _list.size() - 1;
		}
		return false;
	}

	@Override
	public Document next() {
		lotus.domino.Document __current = null;
		Document doc = null;

		try {
			switch(type) {
			case COLLECTION:
				__current = __doc;
				__doc = __col.getNextDocument(__doc);

				doc = _session.getFactory().getDocument(__current);
				break;
				
			case VIEW_ENTRY_COLLECTION:
				__current = __ve.getDocument();
				do {
					lotus.domino.ViewEntry __old = __ve;
					__ve = __vecol.getNextEntry(__ve);
					__old.recycle();
				} while (__ve != null && __ve.getDocument() == null);

				doc = _session.getFactory().getDocument(__current);
				break;
				
			case DOCUMENT_LIST:
				doc = _list.get(++listIndex);
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

	@Override
	public DocumentList asDocumentList() {
		DocumentList _list = null;

		switch (type) {
		case COLLECTION:
		case VIEW_ENTRY_COLLECTION:
			_list = new DefaultDocumentList(_session, this);
			break;
		case DOCUMENT_LIST:
			_list = this._list;
		}

		return _list;
	}

	@Override
	public DocumentIterator deleteAll() {
		for (Document doc: this) {
			doc.delete();
		}

		return this;
	}

	@Override
	public Object getNativeObject() {
		switch(type) {
		case COLLECTION:
			return __col;
		case VIEW_ENTRY_COLLECTION:
			return __ve;
		case DOCUMENT_LIST:
			return null;
		}

		return null;
	}

	@Override
	public boolean isOpen() {
		switch(type) {
		case COLLECTION:
			return __col != null;
		case VIEW_ENTRY_COLLECTION:
			return __ve != null;
		case DOCUMENT_LIST:
			return _list != null;
		}

		return false;
	}

	@Override
	public void close() {

	}

	@Override
	public void finalize() {
		log.finest("Finalized: id=" + objectId + " (" + this.hashCode() + ")");
	}
}
