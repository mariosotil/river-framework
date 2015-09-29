package org.riverframework.wrapper.org.openntf.domino;

import java.util.Iterator;

import org.riverframework.River;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DocumentIteratorFromDocumentCollection extends AbstractBaseOrgOpenntfDomino<org.openntf.domino.DocumentCollection> implements DocumentIterator<org.openntf.domino.DocumentCollection,org.openntf.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;
	private Iterator<org.openntf.domino.Document> __documentIterator = null;

	private Document<org.openntf.domino.Document> _doc = null;
	private org.openntf.domino.Document __document = null;

	protected DocumentIteratorFromDocumentCollection(org.riverframework.wrapper.Session<org.openntf.domino.Session> _session, org.openntf.domino.DocumentCollection __native) {
		super(_session, __native);
		__documentIterator = __native.iterator();
	}

	@Override
	public String calcObjectId(org.openntf.domino.DocumentCollection __object) {
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

	@Override
	public boolean hasNext() {
			return __documentIterator.hasNext();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<org.openntf.domino.Document> next() {
			__document = __documentIterator.next();			
		_doc = (Document<org.openntf.domino.Document>) _factory.getDocument(__document);
		return _doc;
	}

	@Override
	public DocumentIterator<org.openntf.domino.DocumentCollection,org.openntf.domino.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<org.openntf.domino.DocumentCollection,org.openntf.domino.Document> deleteAll() {
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
