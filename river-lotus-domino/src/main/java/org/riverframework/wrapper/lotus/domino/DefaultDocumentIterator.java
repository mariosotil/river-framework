package org.riverframework.wrapper.lotus.domino;

import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DefaultDocumentIterator implements DocumentIterator {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private enum Type { COLLECTION, VIEW_ENTRY_COLLECTION } 

	private org.riverframework.wrapper.Session _session = null;

	private lotus.domino.DocumentCollection __col = null;
	private lotus.domino.ViewEntryCollection __vecol = null;

	private Document _doc = null;
	private lotus.domino.Document __doc = null;
	private lotus.domino.ViewEntry __ve = null;

	private Type type = null;	
	private String objectId = null;

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.DocumentCollection __obj) {
		type = Type.COLLECTION;
		_session = s;
		__col = __obj;

		try {
			__doc = __col.getFirstDocument();
			
			updateCurrentDocumentFromDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		objectId = calcObjectId(__col);
	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.View __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;
		_session = s;		

		try {
			__vecol = __obj.getAllEntries();
			__ve = __vecol.getFirstEntry();

			updateCurrentDocumentFromViewEntry();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		objectId = calcObjectId(__vecol);
	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session s, lotus.domino.ViewEntryCollection __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;		
		_session = s;		
		__vecol = __obj;

		try {
			__ve = __vecol.getFirstEntry();
			
			updateCurrentDocumentFromViewEntry();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		objectId = calcObjectId(__vecol);
	}

	@SuppressWarnings("unchecked")
	private void updateCurrentDocumentFromDocument() {		
		_doc = __doc == null ? null : _session.getFactory().getDocument(__doc);
	}

	@SuppressWarnings("unchecked")
	private void updateCurrentDocumentFromViewEntry() {
		try {
			while (__ve != null && __ve.getDocument() == null) {
				lotus.domino.ViewEntry __old = __ve;
				__ve = __vecol.getNextEntry(__ve);
				__old.recycle();
			} 

			__doc = __ve == null ? null : __ve.getDocument();
			_doc = __doc == null ? null : _session.getFactory().getDocument(__doc);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	private static String internalCalcObjectId(Object __object) {
		StringBuilder sb = new StringBuilder();
		sb.append(__object.getClass().getName());
		sb.append(River.ID_SEPARATOR);
		sb.append(__object.hashCode());

		String objectId = sb.toString();

		return objectId;
	}
	
	public static String calcObjectId(lotus.domino.DocumentCollection __object) {
		return internalCalcObjectId(__object);
	}

	public static String calcObjectId(lotus.domino.ViewEntryCollection __object) {
		return internalCalcObjectId(__object);
	}

	public static String calcObjectId(lotus.domino.View __object) {
		return internalCalcObjectId(__object);
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	@Override
	public boolean hasNext() {
		return _doc != null;
	}

	@Override
	public Document next() {
		Document _current = null;

		try {
			switch(type) {
			case COLLECTION:
				_current = _doc;
				__doc = __col.getNextDocument(__doc);
				
				updateCurrentDocumentFromDocument();
				break;

			case VIEW_ENTRY_COLLECTION:
				_current = _doc;
				__ve = __vecol.getNextEntry(__ve);
				
				updateCurrentDocumentFromViewEntry();
				break;
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return _current;
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
		}

		return false;
	}

	@Override
	public void close() {
		log.finest("Closing: id=" + objectId + " (" + this.hashCode() + ")");

		try {
			if (__col != null) 
				__col.recycle();			
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__col = null;
		}

		try {
			if (__vecol != null) 
				__vecol.recycle();			
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__vecol = null;
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}

	@Override
	public void finalize() {
		log.finest("Finalized: id=" + objectId + " (" + this.hashCode() + ")");
	}
}
