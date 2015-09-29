package org.riverframework.wrapper.org.openntf.domino;

import java.util.Iterator;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DocumentIteratorFromViewEntryCollection extends AbstractBaseOrgOpenntfDomino<org.openntf.domino.ViewEntryCollection> implements DocumentIterator<org.openntf.domino.ViewEntryCollection,org.openntf.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;
	private org.openntf.domino.ViewEntryCollection __native = null;

	private Iterator<org.openntf.domino.ViewEntry> __viewEntryIterator = null;

	private Document<org.openntf.domino.Document> _doc = null;
	private org.openntf.domino.Document __document = null;
	private org.openntf.domino.ViewEntry __viewEntry = null;

	protected DocumentIteratorFromViewEntryCollection(org.riverframework.wrapper.Session<org.openntf.domino.Session> _session, org.openntf.domino.ViewEntryCollection __native) {
		super(_session, __native);
		__viewEntryIterator = __native.iterator();
	}

	@Override
	public String calcObjectId(org.openntf.domino.ViewEntryCollection __object) {
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
		return __viewEntryIterator.hasNext();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<org.openntf.domino.Document> next() {
		__viewEntry = __viewEntryIterator.next();
		updateCurrentDocumentFromViewEntry();

		_doc = (Document<org.openntf.domino.Document>) _factory.getDocument(__document);
		return _doc;
	}

	@Override
	public DocumentIterator<org.openntf.domino.ViewEntryCollection,org.openntf.domino.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<org.openntf.domino.ViewEntryCollection,org.openntf.domino.Document> deleteAll() {
		for (Document<org.openntf.domino.Document> doc: this) {
			doc.delete();
		}

		return this;
	}

	@Override
	public boolean isOpen() {
		return __native != null;
	}

	@Override
	public void close() {

	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
