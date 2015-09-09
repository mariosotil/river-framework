package org.riverframework.wrapper.lotus.domino;

import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DefaultDocumentIterator implements Base<lotus.domino.Base>, DocumentIterator<lotus.domino.Base, lotus.domino.Document> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	DocumentIterator<?, lotus.domino.Document> _iterator = null;
	
	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<lotus.domino.Session> _session, lotus.domino.DocumentCollection __native) {
		_iterator = new DocumentIteratorFromDocumentCollection(_session, __native);
	}

	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<lotus.domino.Session> _session, lotus.domino.ViewEntryCollection __native) {
		_iterator = new DocumentIteratorFromViewEntryCollection(_session, __native);
	}

	@Override
	public boolean hasNext() {
		return _iterator.hasNext();
	}

	@Override
	public Document<lotus.domino.Document> next() {
		return _iterator.next();
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> deleteAll() {
		_iterator.deleteAll();
		
		return this;
	}

	@Override
	public lotus.domino.Base getNativeObject() {
		return (lotus.domino.Base) _iterator.getNativeObject();
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

	@Override
	public String getObjectId() {
		return _iterator.getObjectId();
	}

}
