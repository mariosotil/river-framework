package org.riverframework.wrapper.com.couchbase.lite;

import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;

class DefaultDocumentIterator implements Base<Object>, DocumentIterator<Object,com.couchbase.lite.Document> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;
	DocumentIterator<?, com.couchbase.lite.Document> _iterator = null;
	
	protected DefaultDocumentIterator(org.riverframework.wrapper.Session<com.couchbase.lite.Manager> _session, Object __native) {
		//_iterator = new DocumentIteratorFromDocumentCollection(_session, __native);
	}

	@Override
	public String getObjectId() {
		return _iterator.getObjectId();
	}

	@Override
	public Object getNativeObject() {
		return (Object) _iterator.getNativeObject();
	}
	
	@Override
	public boolean hasNext() {
		return _iterator.hasNext();
	}

	@Override
	public Document<com.couchbase.lite.Document> next() {
		return _iterator.next();
	}

	@Override
	public DocumentIterator<Object,com.couchbase.lite.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<Object,com.couchbase.lite.Document> deleteAll() {
		for (Document<com.couchbase.lite.Document> doc: this) {
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
