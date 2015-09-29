package org.riverframework.wrapper.lotus.domino;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DocumentIteratorFromDocumentCollection extends AbstractBaseLotusDomino<lotus.domino.DocumentCollection> implements DocumentIterator<lotus.domino.DocumentCollection, lotus.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	private lotus.domino.Document __document = null;
	private Document<lotus.domino.Document> _doc = null;

	@SuppressWarnings("unchecked")
	protected DocumentIteratorFromDocumentCollection(org.riverframework.wrapper.Session<lotus.domino.Session> _session, lotus.domino.DocumentCollection __native) {
		super(_session, __native);

		try {
			__document = __native.getFirstDocument();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		_doc = (Document<lotus.domino.Document>) _factory.getDocument(__document); //Document<lotus.domino.Base>
	}

	public boolean isRecycled() {
		return isObjectRecycled(__native);
	}

	public String calcObjectId(lotus.domino.DocumentCollection __object) {
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
			__document = __native.getNextDocument(__document);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		_doc = (Document<lotus.domino.Document>) _factory.getDocument(__document); //Document<lotus.domino.Base>

		return _current;
	}

	@Override
	public DocumentIterator<lotus.domino.DocumentCollection, lotus.domino.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<lotus.domino.DocumentCollection, lotus.domino.Document> deleteAll() {
		for (Document<lotus.domino.Document> doc: this) {
			doc.delete();
		}

		return this;
	}

	@Override
	public boolean isOpen() {
		return __native != null; // && !isRecycled(__documentCollection);
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
