package local.wrapper;

import local.mock.DatabaseException;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;
import org.riverframework.wrapper.Session;

class DefaultView extends AbstractBaseNoSQL<local.mock.View> implements View<local.mock.View> {
	protected DefaultView(Session<local.mock.Session> _session, local.mock.View __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(local.mock.View __view) {
		String objectId = "";

		if (__view != null) { // && !isRecycled(__view)) {
			try {
				local.mock.Database __database = __view.getParent();

				StringBuilder sb = new StringBuilder(1024);
				sb.append(__database.getServer());
				sb.append(River.ID_SEPARATOR);
				sb.append(__database.getFilePath());
				sb.append(River.ID_SEPARATOR);
				sb.append(__view.getName());

				objectId = sb.toString();
			} catch (DatabaseException e) {
				throw new RiverException(e);
			}	
		}

		return objectId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<local.mock.Document> getDocumentByKey(String key) {
		local.mock.Document __doc = null;

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

		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		Document<local.mock.Document> doc = (Document<local.mock.Document>) _factory.getDocument(__doc);

		return doc;
	}

	public boolean isRecycled() {
		return AbstractBaseNoSQL.isObjectRecycled(__native);
	}

	@Override
	public boolean isOpen() {
		return __native != null && !isRecycled();
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> getAllDocuments() {
		local.mock.DocumentCollection __vecol;
		try {
			__vecol = __native.getAllEntries();

		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<local.mock.Base, local.mock.Document> result = (DocumentIterator<local.mock.Base, local.mock.Document>) _factory.getDocumentIterator(__vecol);
		return result;
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> getAllDocumentsByKey(Object key) {
		local.mock.DocumentCollection _col;
		try {
			_col = __native.getAllDocumentsByKey(key, true);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<local.mock.Base, local.mock.Document> result = (DocumentIterator<local.mock.Base, local.mock.Document>) _factory.getDocumentIterator(_col);
		return result;
	}

	@Override
	public View<local.mock.View> refresh() {
		try {
			__native.refresh();
		} catch (DatabaseException e) {
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
			} catch (DatabaseException e) {
				throw new RiverException(e);
			} finally {
				__native = null;
			}
		}
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> search(String query) {
		DocumentIterator<local.mock.Base, local.mock.Document> _iterator = search(query, 0);

		return _iterator;
	}

	@Override
	public DocumentIterator<local.mock.Base, local.mock.Document> search(String query, int max) {
		local.mock.View __temp = null;

		try {
			__temp = __native.getParent().getView(__native.getName());			
			__temp.FTSearch(query, max);
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<local.mock.Base, local.mock.Document> _iterator = (DocumentIterator<local.mock.Base, local.mock.Document>) _factory.getDocumentIterator(__temp);

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
