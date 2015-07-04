package org.riverframework.wrapper.lotus.domino;

import org.riverframework.River;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DefaultDocumentIterator extends DefaultBase<org.openntf.domino.Base<?>> implements DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private enum Type { COLLECTION, VIEW_ENTRY_COLLECTION } 

	private org.riverframework.wrapper.Session<org.openntf.domino.Session> _session = null;

	private volatile org.openntf.domino.DocumentCollection __documentCollection = null;
	private volatile org.openntf.domino.ViewEntryCollection __viewEntryCollection = null;

	// private Document<org.openntf.domino.Document> _doc = null;
	private volatile org.openntf.domino.Document __document = null;
	private volatile org.openntf.domino.ViewEntry __viewEntry = null;

	private Type type = null;	
	private String objectId = null;

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> s, org.openntf.domino.DocumentCollection __obj) {
		type = Type.COLLECTION;
		_session = s;
		__documentCollection = __obj;

		synchronized (_session){
			__document = __documentCollection.getFirstDocument();

			objectId = calcObjectId(__documentCollection);
		}

	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> s, org.openntf.domino.View __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;
		_session = s;		

		synchronized (_session){
			__viewEntryCollection = __obj.getAllEntries();
			__viewEntry = __viewEntryCollection.getFirstEntry();

			objectId = calcObjectId(__viewEntryCollection);
		}

	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> s, org.openntf.domino.ViewEntryCollection __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;		
		_session = s;		
		__viewEntryCollection = __obj;

		synchronized (_session){
			__viewEntry = __viewEntryCollection.getFirstEntry();

			objectId = calcObjectId(__viewEntryCollection);
		}
	}

	private static String internalCalcObjectId(org.openntf.domino.Base<?> __object) {
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

	@SuppressWarnings("deprecation")
	@Override
	public Document<org.openntf.domino.Document> next() {
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
				break;
			}

			@SuppressWarnings("unchecked")
			Document<org.openntf.domino.Document> _doc = _session.getFactory().getDocument(__current);
			return _doc;
		}
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> deleteAll() {
		for (Document<org.openntf.domino.Document> doc: this) {
			doc.delete();
		}

		return this;
	}

	@Override
	public org.openntf.domino.Base<?> getNativeObject() {
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

	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
