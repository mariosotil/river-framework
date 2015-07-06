package org.riverframework.wrapper.org.openntf.domino;

import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.domino.Base;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Factory;

class DefaultDocumentIterator extends DefaultBase<org.openntf.domino.Base<?>> implements DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;
	private enum Type { COLLECTION, VIEW_ENTRY_COLLECTION } 

	private org.riverframework.wrapper.Session<org.openntf.domino.Session> _session = null;
	protected org.riverframework.wrapper.Factory<org.openntf.domino.Base<?>> _factory = null;

	private org.openntf.domino.DocumentCollection __documentCollection = null;
	private org.openntf.domino.ViewEntryCollection __viewEntryCollection = null;

	private Iterator<org.openntf.domino.Document> __documentIterator = null;
	private Iterator<org.openntf.domino.ViewEntry> __viewEntryIterator = null;

	private Document<org.openntf.domino.Document> _doc = null;
	private org.openntf.domino.Document __document = null;
	private org.openntf.domino.ViewEntry __viewEntry = null;

	private Type type = null;	
	private String objectId = null;

	@SuppressWarnings("unchecked")
	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> s, org.openntf.domino.DocumentCollection __obj) {
		type = Type.COLLECTION;
		_session = s;
		_factory = (Factory<Base<?>>) _session.getFactory();
		__documentCollection = __obj;
		__documentIterator = __documentCollection.iterator();
		objectId = calcObjectId(__documentCollection);
	}

	@SuppressWarnings("unchecked")
	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> s, org.openntf.domino.View __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;
		_session = s;		
		_factory = (Factory<Base<?>>) _session.getFactory();
		__viewEntryCollection = __obj.getAllEntries();
		__viewEntryIterator = __viewEntryCollection.iterator();
		objectId = calcObjectId(__viewEntryCollection);
	}

	@SuppressWarnings("unchecked")
	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> s, org.openntf.domino.ViewEntryCollection __obj) {
		type = Type.VIEW_ENTRY_COLLECTION;		
		_session = s;		
		_factory = (Factory<Base<?>>) _session.getFactory();
		__viewEntryCollection = __obj;
		__viewEntryIterator = __viewEntryCollection.iterator();
		objectId = calcObjectId(__viewEntryCollection);
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

	private boolean isViewEntryValid(lotus.domino.ViewEntry __ve){
		if(__ve == null) return true;

		try {
			lotus.domino.Document __doc = __ve.getDocument();

			if (__doc == null) return false;
			if (__doc.isDeleted()) return false;

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return true;
	}

	private void updateCurrentDocumentFromViewEntry() {
		while (__viewEntryIterator.hasNext() && !isViewEntryValid(__viewEntry)) 
		{
			__viewEntry = __viewEntryIterator.next();
		} 

		__document = __viewEntry == null ? null : __viewEntry.getDocument();				
	}

	@Override
	public boolean hasNext() {
		switch(type) {
		case COLLECTION:
			return __documentIterator.hasNext();
		case VIEW_ENTRY_COLLECTION:
			return __viewEntryIterator.hasNext();
		default:
			throw new RiverException("Wrong iterator type");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<org.openntf.domino.Document> next() {
		switch(type) {
		case COLLECTION:
			__document = __documentIterator.next();			
			break;

		case VIEW_ENTRY_COLLECTION:
			__viewEntry = __viewEntryIterator.next();
			updateCurrentDocumentFromViewEntry();
			break;
		default:
			throw new RiverException("Wrong iterator type");
		}

		_doc = (Document<org.openntf.domino.Document>) _factory.getDocument(__document);
		return _doc;
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
		default:
			throw new RiverException("Wrong iterator type");
		}
	}

	@Override
	public boolean isOpen() {
		switch(type) {
		case COLLECTION:
			return __documentCollection != null;
		case VIEW_ENTRY_COLLECTION:
			return __viewEntryCollection != null;
		default:
			throw new RiverException("Wrong iterator type");
		}
	}

	@Override
	public void close() {

	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
