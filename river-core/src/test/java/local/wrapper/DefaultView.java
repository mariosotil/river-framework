package local.wrapper;

import local.mock.*;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;
import org.riverframework.wrapper.Session;

class DefaultView extends AbstractBaseWrapper<ViewMock> implements View<ViewMock> {
	protected DefaultView(Session<SessionMock> _session, ViewMock __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(ViewMock __view) {
		String objectId = "";

		if (__view != null) { // && !isRecycled(__view)) {
			try {
				DatabaseMock __database = __view.getParent();

				StringBuilder sb = new StringBuilder(1024);
				sb.append(__database.getServer());
				sb.append(River.ID_SEPARATOR);
				sb.append(__database.getFilePath());
				sb.append(River.ID_SEPARATOR);
				sb.append(__view.getName());

				objectId = sb.toString();
			} catch (MockException e) {
				throw new RiverException(e);
			}	
		}

		return objectId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<DocumentMock> getDocumentByKey(String key) {
		DocumentMock __doc = null;

		try {
			__doc = __native.getDocumentByKey(key, true);
			while (__doc != null && __doc.isDeleted()) 
			{
				// TODO: register this documents into the cache to avoid the conflict after recycling
				// org.riverframework.mock.Document __deleted = __doc;
				__doc = __native.getNextDocument(__doc);
				// __deleted.recycle();  <== Bad idea
			}

			// if (__doc != null && !__doc.getColumnValues().get(0).equals(key)) __doc = null;

		} catch (MockException e) {
			throw new RiverException(e);
		}

		Document<DocumentMock> doc = (Document<DocumentMock>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public boolean isOpen() {
		return __native != null;
	}

	@Override
	public DocumentIterator<BaseMock, DocumentMock> getAllDocuments() {
		DocumentCollectionMock __vecol;
		try {
			__vecol = __native.getAllEntries();

		} catch (MockException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<BaseMock, DocumentMock> result = (DocumentIterator<BaseMock, DocumentMock>) _factory.getDocumentIterator(__vecol);
		return result;
	}

	@Override
	public DocumentIterator<BaseMock, DocumentMock> getAllDocumentsByKey(Object key) {
		DocumentCollectionMock _col;
		try {
			_col = __native.getAllDocumentsByKey(key, true);
		} catch (MockException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<BaseMock, DocumentMock> result = (DocumentIterator<BaseMock, DocumentMock>) _factory.getDocumentIterator(_col);
		return result;
	}

	@Override
	public View<ViewMock> refresh() {
		try {
			__native.refresh();
		} catch (MockException e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public void delete() {
		if (__native != null) {
			try {
				__native.remove();
				// __view.recycle();  <== Let the server recycle
			} catch (MockException e) {
				throw new RiverException(e);
			} finally {
				__native = null;
			}
		}
	}

	@Override
	public DocumentIterator<BaseMock, DocumentMock> search(String query) {
		DocumentIterator<BaseMock, DocumentMock> _iterator = search(query, 0);

		return _iterator;
	}

	@Override
	public DocumentIterator<BaseMock, DocumentMock> search(String query, int max) {
		ViewMock __temp = null;

		try {
			__temp = __native.getParent().getView(__native.getName());			
			__temp.FTSearch(query, max);
		} catch (MockException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<BaseMock, DocumentMock> _iterator = (DocumentIterator<BaseMock, DocumentMock>) _factory.getDocumentIterator(__temp);

		return _iterator;
	}

	@Override
	@Deprecated
	public void close() {
		// Don't recycle or close it. Let the server do that.
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
