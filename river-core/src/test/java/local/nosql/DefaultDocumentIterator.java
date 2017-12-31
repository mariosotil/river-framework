package local.nosql;


import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Base;

class DefaultDocumentIterator implements Base<local.mock.Base>, DocumentIterator<local.mock.Base, local.mock.Document> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	DocumentIterator<?, local.mock.Document> _iterator = null;
	
	protected DefaultDocumentIterator(Session<local.mock.Session> _session, local.mock.DocumentCollection __native) {
		_iterator = new DocumentIteratorFromDocumentCollection(_session, __native);
	}

	@Override
	public boolean hasNext() {
		return _iterator.hasNext();
	}

	@Override
	public Document<local.mock.Document> next() {
		return _iterator.next();
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> deleteAll() {
		_iterator.deleteAll();
		
		return this;
	}

	@Override
	public local.mock.Base getNativeObject() {
		return (local.mock.Base) _iterator.getNativeObject();
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
