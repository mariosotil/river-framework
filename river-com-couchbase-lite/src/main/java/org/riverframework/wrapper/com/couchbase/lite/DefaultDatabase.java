package org.riverframework.wrapper.com.couchbase.lite;

// import java.util.logging.Level;
// import java.util.logging.Logger;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

import com.couchbase.lite.CouchbaseLiteException;

class DefaultDatabase extends AbstractBaseCouchbaseLite<com.couchbase.lite.Database> implements org.riverframework.wrapper.Database<com.couchbase.lite.Database> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	protected DefaultDatabase(org.riverframework.wrapper.Session<com.couchbase.lite.Manager> _session, com.couchbase.lite.Database __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(com.couchbase.lite.Database __database) {
		String objectId = "";

		if (__database != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("LOCAL");
			sb.append(River.ID_SEPARATOR);
			sb.append(__database.getPath());

			objectId = sb.toString();
		} 

		return objectId;
	}

	@Override
	public String getServer() {
		return "LOCAL";
	}

	@Override
	public String getFilePath() {
		return __native.getPath();
	}

	@Override
	public String getName() {
		return __native.getName();
	}

	@Override
	public boolean isOpen() {
		return (__native != null && __native.isOpen());
	}

	@Override
	public Document<com.couchbase.lite.Document> createDocument(String... parameters) {
		com.couchbase.lite.Document __doc = null;

		__doc = __native.createDocument();

		@SuppressWarnings("unchecked")
		Document<com.couchbase.lite.Document> doc = (Document<com.couchbase.lite.Document>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public Document<com.couchbase.lite.Document> getDocument(String... parameters)
	{
		com.couchbase.lite.Document __doc = null;

		@SuppressWarnings("unchecked")
		Document<com.couchbase.lite.Document> doc = (Document<com.couchbase.lite.Document>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public View<com.couchbase.lite.View> createView(String... parameters) {
		String name = null;
		View<com.couchbase.lite.View> _view = getView(name);
		return _view;
	}

	@Override
	public View<com.couchbase.lite.View> getView(String... parameters) {
		com.couchbase.lite.View __view = null;

		@SuppressWarnings("unchecked")
		View<com.couchbase.lite.View> _view = (View<com.couchbase.lite.View>) _factory.getView(__view);
		return _view;
	}

	@Override
	public DocumentIterator<Object,com.couchbase.lite.Document> getAllDocuments() {
		DocumentIterator<Object,com.couchbase.lite.Document> _iterator = null; 
		return _iterator;
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
	public Database<com.couchbase.lite.Database> refreshSearchIndex(boolean createIfNotExist) {
		return this;
	}

	@Override
	public void delete() {
		if (__native != null)
			try {
				__native.delete();
			} catch (CouchbaseLiteException e) {
				throw new RiverException(e);
			}			

		close();
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}

	@Override
	public void close() {
		// Don't recycle or close it. Let the server do that.
	}
}
