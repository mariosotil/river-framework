package org.riverframework.wrapper.lotus.domino;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DefaultDocumentIterator extends DefaultBase implements DocumentIterator<lotus.domino.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private enum Type { COLLECTION, VIEW_ENTRY_COLLECTION } 

	private org.riverframework.wrapper.Session<lotus.domino.Base> _session = null;

	private volatile lotus.domino.DocumentCollection __documentCollection = null;
	private volatile lotus.domino.ViewEntryCollection __viewEntryCollection = null;

	//private Document<lotus.domino.Base> _doc = null;
	private volatile lotus.domino.Document __document = null;
	private volatile lotus.domino.ViewEntry __viewEntry = null;

	private Type type = null;	
	private String objectId = null;

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<lotus.domino.Base> s, lotus.domino.DocumentCollection __obj) {
		type = Type.COLLECTION;
		_session = s;
		__documentCollection = __obj;

		synchronized (_session){
			try {
				__document = __documentCollection.getFirstDocument();

			} catch (NotesException e) {
				throw new RiverException(e);
			}
			objectId = calcObjectId(__documentCollection);
		}

	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<lotus.domino.Base> s, lotus.domino.View __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;
		_session = s;		

		synchronized (_session){
			try {
				__viewEntryCollection = __obj.getAllEntries();
				__viewEntry = __viewEntryCollection.getFirstEntry();

				updateCurrentDocumentFromViewEntry();
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			objectId = calcObjectId(__viewEntryCollection);
		}

	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<lotus.domino.Base> s, lotus.domino.ViewEntryCollection __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;		
		_session = s;		
		__viewEntryCollection = __obj;

		synchronized (_session){
			try {
				__viewEntry = __viewEntryCollection.getFirstEntry();

				updateCurrentDocumentFromViewEntry();
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			objectId = calcObjectId(__viewEntryCollection);
		}
	}

	//	@SuppressWarnings("unchecked")
	//	private void updateCurrentDocumentFromDocument() {		
	//		synchronized (_session){
	//			_doc = __doc == null ? null : _session.getFactory().getDocument(__doc);
	//		}
	//	}

	private boolean isViewEntryValid(lotus.domino.ViewEntry __ve){
		if(__ve == null) return true;

		try {
			lotus.domino.Document __doc = __ve.getDocument();

			if (__doc == null) return false;

			if (isRecycled(__ve.getDocument())) return false;

			if (__doc.isDeleted()) return false;

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private void updateCurrentDocumentFromViewEntry() {
		synchronized (_session){
			try {
				while (!isViewEntryValid(__viewEntry)) 
				{
					// lotus.domino.ViewEntry __old = __ve;
					__viewEntry = __viewEntryCollection.getNextEntry(__viewEntry);
					// CHECKING __old.recycle();  // To recycle or not to recycle... That is the question.
				} 

				log.finest("Current view entry=" + (__viewEntry == null ? "<null>" :__viewEntry.getDocument().hashCode()));

				__document = __viewEntry == null ? null : __viewEntry.getDocument();				
			} catch (NotesException e) {
				throw new RiverException(e);
			}
		}
	}

	private static String internalCalcObjectId(lotus.domino.Base __object) {
		String objectId = "";
		if (__object != null && !isRecycled(__object)) {

			StringBuilder sb = new StringBuilder();
			sb.append(__object.getClass().getName());
			sb.append(River.ID_SEPARATOR);
			sb.append(__object.hashCode());

			objectId = sb.toString();
		}
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
		return __document != null;
	}

	@Override
	public Document<lotus.domino.Base> next() {
		lotus.domino.Document __current = null;

		synchronized (_session){
			try {
				switch(type) {
				case COLLECTION:
					__current = __document;
					__document = __documentCollection.getNextDocument(__document);
					break;

				case VIEW_ENTRY_COLLECTION:
					__current = __document;
					__viewEntry = __viewEntryCollection.getNextEntry(__viewEntry);
					updateCurrentDocumentFromViewEntry();
					break;
				}
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			Document<lotus.domino.Base> _doc = _session.getFactory().getDocument(__current);
			return _doc;
		}
	}

	@Override
	public DocumentIterator<lotus.domino.Base> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<lotus.domino.Base> deleteAll() {
		for (Document<lotus.domino.Base> doc: this) {
			doc.delete();
		}

		return this;
	}

	@Override
	public lotus.domino.Base getNativeObject() {
		switch(type) {
		case COLLECTION:
			return __documentCollection;
		case VIEW_ENTRY_COLLECTION:
			return __viewEntryCollection;
		}

		return null;
	}

	@Override
	public boolean isOpen() {
		switch(type) {
		case COLLECTION:
			return __documentCollection != null && !isRecycled(__documentCollection);
		case VIEW_ENTRY_COLLECTION:
			return __viewEntryCollection != null && !isRecycled(__viewEntryCollection);
		}

		return false;
	}

	@Override
	public void close() {
		log.finest("Closing: id=" + objectId + " (" + this.hashCode() + ")");

		try {
			// CHECKING if (__col != null) __col.recycle();		// To recycle or not to recycle... That is the question.	
			// } catch (NotesException e) {
			// throw new RiverException(e);
		} finally {
			// __documentCollection = null;
		}

		try {
			// CHECKING if (__vecol != null) __vecol.recycle(); // To recycle or not to recycle... That is the question.
			// } catch (NotesException e) {
			// throw new RiverException(e);
		} finally {
			// __viewEntryCollection = null;
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
