package local.wrapper;


import local.mock.BaseMock;
import local.mock.DocumentCollectionMock;
import local.mock.DocumentMock;
import local.mock.SessionMock;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Base;

class DefaultDocumentIterator implements Base<BaseMock>, DocumentIterator<BaseMock, DocumentMock> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	DocumentIterator<?, DocumentMock> _iterator = null;
	
	protected DefaultDocumentIterator(Session<SessionMock> _session, DocumentCollectionMock __native) {
		_iterator = new DocumentIteratorFromDocumentCollection(_session, __native);
	}

	@Override
	public boolean hasNext() {
		return _iterator.hasNext();
	}

	@Override
	public Document<DocumentMock> next() {
		return _iterator.next();
	}

	@Override
	public DocumentIterator<BaseMock, DocumentMock> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();

	}

	@Override
	public DocumentIterator<BaseMock, DocumentMock> deleteAll() {
		_iterator.deleteAll();
		
		return this;
	}

	@Override
	public BaseMock getNativeObject() {
		return (BaseMock) _iterator.getNativeObject();
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
