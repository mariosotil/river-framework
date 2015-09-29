package org.riverframework.wrapper.com.couchbase.lite;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

import com.couchbase.lite.CouchbaseLiteException;

class DefaultView extends AbstractBaseCouchbaseLite<com.couchbase.lite.View> implements org.riverframework.wrapper.View<com.couchbase.lite.View> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	protected DefaultView(org.riverframework.wrapper.Session<com.couchbase.lite.Manager> _session, com.couchbase.lite.View __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(com.couchbase.lite.View __view) {
		String objectId = "";

		if (__view != null) {
			com.couchbase.lite.Database __database = __view.getDatabase();

			StringBuilder sb = new StringBuilder();
			sb.append("LOCAL");
			sb.append(River.ID_SEPARATOR);
			sb.append(__database.getPath());
			sb.append(River.ID_SEPARATOR);
			sb.append(__view.getName());

			objectId = sb.toString();
		}

		return objectId;
	}

	@Override
	public Document<Object> getDocumentByKey(String key) {
		com.couchbase.lite.Document __doc = null;

		// __doc = __native.getDocumentByKey(key, true); How implements this couchbase?

		@SuppressWarnings("unchecked")
		Document<Object> doc = (Document<Object>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public boolean isOpen() {
		return __native != null;
	}

	@Override
	public DocumentIterator<Object,com.couchbase.lite.Document> getAllDocuments() {
		
		DocumentIterator<Object,com.couchbase.lite.Document> result = null; 
		return result;
	}

	@Override
	public DocumentIterator<Object,com.couchbase.lite.Document> getAllDocumentsByKey(Object key) {
		DocumentIterator<Object,com.couchbase.lite.Document> result = null; 
		return result;
	}

	@Override
	public View<com.couchbase.lite.View> refresh() {
		try {
			__native.updateIndex();
		} catch (CouchbaseLiteException e) {
			throw new RiverException(e);
		}
		return this;
	}

	@Override
	public void delete() {
		if (__native != null) {
			__native.delete();
			__native = null;
		}
	}

	@Override
	public DocumentIterator<Object,com.couchbase.lite.Document> search(String query) {
		DocumentIterator<Object,com.couchbase.lite.Document> _iterator = search(query, 0); 
		return _iterator;
	}

	@Override
	public DocumentIterator<Object,com.couchbase.lite.Document> search(String query, int max) {
		DocumentIterator<Object,com.couchbase.lite.Document> _iterator = null; 
		return _iterator;
	}

	@Override
	public View<com.couchbase.lite.View> addColumn(String name, String value, boolean isSorted) {
		return this;
	}

	@Override
	public void close() {
		// Don't recycle or close it. Let the server do that.
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
