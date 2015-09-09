package org.riverframework.wrapper.org.openntf.domino;

import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DefaultDocumentIterator implements Base<org.openntf.domino.Base<?>>, DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;
	DocumentIterator<?, org.openntf.domino.Document> _iterator = null;
	
	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> _session, org.openntf.domino.DocumentCollection __native) {
		_iterator = new DocumentIteratorFromDocumentCollection(_session, __native);
	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<org.openntf.domino.Session> _session, org.openntf.domino.ViewEntryCollection __native) {
		_iterator = new DocumentIteratorFromViewEntryCollection(_session, __native);
	}
	
	@Override
	public String getObjectId() {
		return _iterator.getObjectId();
	}

	@Override
	public org.openntf.domino.Base<?> getNativeObject() {
		return (org.openntf.domino.Base<?>) _iterator.getNativeObject();
	}
	
	@Override
	public boolean hasNext() {
		return _iterator.hasNext();
	}

	@Override
	public Document<org.openntf.domino.Document> next() {
		return _iterator.next();
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
	public boolean isOpen() {
			return _iterator.isOpen();
	}

	@Override
	@Deprecated
	public void close() {

	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + _iterator.getObjectId() + ")";
	}
}
