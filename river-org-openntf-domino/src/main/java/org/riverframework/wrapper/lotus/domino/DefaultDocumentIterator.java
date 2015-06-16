package org.riverframework.wrapper.lotus.domino;

import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DefaultDocumentIterator extends DefaultBase implements DocumentIterator<org.openntf.domino.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private enum Type { COLLECTION, VIEW_ENTRY_COLLECTION } 

	private org.riverframework.wrapper.Session<org.openntf.domino.Base> _session = null;

	private volatile org.openntf.domino.DocumentCollection __documentCollection = null;
	private volatile org.openntf.domino.ViewEntryCollection __viewEntryCollection = null;

	//private Document<org.openntf.domino.Base> _doc = null;
	private volatile org.openntf.domino.Document __document = null;
	private volatile org.openntf.domino.ViewEntry __viewEntry = null;

	private Type type = null;	
	private String objectId = null;

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Base> s, org.openntf.domino.DocumentCollection __obj) {
		type = Type.COLLECTION;
		_session = s;
		__documentCollection = __obj;

		synchronized (_session){
			__document = __documentCollection.getFirstDocument();

			objectId = calcObjectId(__documentCollection);
		}

	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Base> s, org.openntf.domino.View __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;
		_session = s;		

		synchronized (_session){
			__viewEntryCollection = __obj.getAllEntries();
			__viewEntry = __viewEntryCollection.getFirstEntry();

			updateCurrentDocumentFromViewEntry();

			objectId = calcObjectId(__viewEntryCollection);
		}

	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Base> s, org.openntf.domino.ViewEntryCollection __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;		
		_session = s;		
		__viewEntryCollection = __obj;

		synchronized (_session){
			__viewEntry = __viewEntryCollection.getFirstEntry();

			updateCurrentDocumentFromViewEntry();

			objectId = calcObjectId(__viewEntryCollection);
		}
	}

	private boolean isViewEntryValid(org.openntf.domino.ViewEntry __ve){
		if(__ve == null) return true;

		org.openntf.domino.Document __doc = __ve.getDocument();

		if (__doc == null) return false;

		if (__doc.isDeleted()) return false;

		return true;
	}

	private void updateCurrentDocumentFromViewEntry() {
		synchronized (_session){
			while (!isViewEntryValid(__viewEntry)) 
			{
				// org.openntf.domino.ViewEntry __old = __ve;
				__viewEntry = __viewEntryCollection.getNextEntry(__viewEntry);
				// CHECKING __old.recycle();  // To recycle or not to recycle... That is the question.
			} 

			log.finest("Current view entry=" + (__viewEntry == null ? "<null>" :__viewEntry.getDocument().hashCode()));

			__document = __viewEntry == null ? null : __viewEntry.getDocument();				
		}
	}

	private static String internalCalcObjectId(org.openntf.domino.Base __object) {
		String objectId = "";
		if (__object != null) {

			StringBuilder sb = new StringBuilder();
			sb.append(__object.getClass().getName());
			sb.append(River.ID_SEPARATOR);
			sb.append(__object.hashCode());

			objectId = sb.toString();
		}
		return objectId;
	}

	public static String calcObjectId(org.openntf.domino.DocumentCollection __object) {
		return internalCalcObjectId(__object);
	}

	public static String calcObjectId(org.openntf.domino.ViewEntryCollection __object) {
		return internalCalcObjectId(__object);
	}

	public static String calcObjectId(org.openntf.domino.View __object) {
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
	public Document<org.openntf.domino.Base> next() {
		org.openntf.domino.Document __current = null;

		synchronized (_session){
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

			@SuppressWarnings("unchecked")
			Document<org.openntf.domino.Base> _doc = _session.getFactory().getDocument(__current);
			return _doc;
		}
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<org.openntf.domino.Base> deleteAll() {
		for (Document<org.openntf.domino.Base> doc: this) {
			doc.delete();
		}

		return this;
	}

	@Override
	public org.openntf.domino.Base getNativeObject() {
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
			return __documentCollection != null;
		case VIEW_ENTRY_COLLECTION:
			return __viewEntryCollection != null;
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
