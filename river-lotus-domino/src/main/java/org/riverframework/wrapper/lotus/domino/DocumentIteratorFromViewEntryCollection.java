package org.riverframework.wrapper.lotus.domino;

import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DocumentIteratorFromViewEntryCollection extends AbstractBaseLotusDomino<lotus.domino.ViewEntryCollection> implements DocumentIterator<lotus.domino.ViewEntryCollection, lotus.domino.Document> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	private lotus.domino.Document __document = null;
	private lotus.domino.ViewEntry __viewEntry = null;

	private Document<lotus.domino.Document> _doc = null;

	@SuppressWarnings("unchecked")
	protected DocumentIteratorFromViewEntryCollection(org.riverframework.wrapper.Session<lotus.domino.Session> _session, lotus.domino.ViewEntryCollection __native) {
		super(_session, __native);

		try {
			__viewEntry = __native.getFirstEntry();

			updateCurrentDocumentFromViewEntry();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		_doc = (Document<lotus.domino.Document>) _factory.getDocument(__document); //Document<lotus.domino.Base>
	}

	@Override
	public boolean isRecycled() {
		return isObjectRecycled(__native);
	}

	private boolean isViewEntryValid(lotus.domino.ViewEntry __ve){
		if(__ve == null) return true;

		try {
			lotus.domino.Document __doc = __ve.getDocument();

			if (__doc == null) return false;

			if (isObjectRecycled(__ve.getDocument())) return false;

			if (__doc.isDeleted()) return false;

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return true;
	}

	private void updateCurrentDocumentFromViewEntry() {
		try {
			while (!isViewEntryValid(__viewEntry)) 
			{
				__viewEntry = __native.getNextEntry(__viewEntry);
			} 

			log.finest("Current view entry=" + (__viewEntry == null ? "<null>" :__viewEntry.getDocument().hashCode()));

			__document = __viewEntry == null ? null : __viewEntry.getDocument();				
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	public String calcObjectId(lotus.domino.ViewEntryCollection __object) {
		String objectId = "";
		if (__object != null) { // && !isRecycled(__object)) {

			StringBuilder sb = new StringBuilder();
			sb.append(__object.getClass().getName());
			sb.append(River.ID_SEPARATOR);
			sb.append(__object.hashCode());

			objectId = sb.toString();
		}
		return objectId;
	}

	@Override
	public boolean hasNext() {
		return __document != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<lotus.domino.Document> next() {
		Document<lotus.domino.Document> _current = _doc;

		try {
			__viewEntry = __native.getNextEntry(__viewEntry);
			updateCurrentDocumentFromViewEntry();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		_doc = (Document<lotus.domino.Document>) _factory.getDocument(__document); //Document<lotus.domino.Base>

		return _current;
	}

	@Override
	public DocumentIterator<lotus.domino.ViewEntryCollection, lotus.domino.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<lotus.domino.ViewEntryCollection, lotus.domino.Document> deleteAll() {
		for (Document<lotus.domino.Document> doc: this) {
			doc.delete();
		}

		return this;
	}

	@Override
	public boolean isOpen() {
		return __native != null; // && !isRecycled(__viewEntryCollection);
	}

	@Override
	@Deprecated
	public void close() {

	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
