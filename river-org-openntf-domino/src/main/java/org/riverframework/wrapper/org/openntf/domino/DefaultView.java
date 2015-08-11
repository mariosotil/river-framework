package org.riverframework.wrapper.org.openntf.domino;

import org.openntf.domino.Base;
import org.openntf.domino.ViewColumn;
import org.riverframework.River;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.View;

class DefaultView extends DefaultBase<org.openntf.domino.View> implements org.riverframework.wrapper.View<org.openntf.domino.View> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;
	protected org.riverframework.wrapper.Session<org.openntf.domino.Session> _session = null;
	protected org.riverframework.wrapper.Factory<org.openntf.domino.Base<?>> _factory = null;
	protected volatile org.openntf.domino.View __view = null;
	private String objectId = null;

	@SuppressWarnings("unchecked")
	protected DefaultView(org.riverframework.wrapper.Session<org.openntf.domino.Session> s, org.openntf.domino.View v) {
		__view = v;
		_session = s;
		_factory = (Factory<Base<?>>) _session.getFactory();
		objectId = calcObjectId(__view);
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	public static String calcObjectId(org.openntf.domino.View __view) {
		String objectId = "";

		if (__view != null) {
			org.openntf.domino.Database __database = __view.getParent();

			StringBuilder sb = new StringBuilder();
			sb.append(__database.getServer());
			sb.append(River.ID_SEPARATOR);
			sb.append(__database.getFilePath());
			sb.append(River.ID_SEPARATOR);
			sb.append(__view.getName());

			objectId = sb.toString();
		}

		return objectId;
	}

	@Override
	public org.openntf.domino.View getNativeObject() {
		return __view;
	}

	@Override
	public Document<org.openntf.domino.Base<?>> getDocumentByKey(String key) {
		org.openntf.domino.Document __doc = null;

		__doc = __view.getDocumentByKey(key, true);

		@SuppressWarnings("unchecked")
		Document<org.openntf.domino.Base<?>> doc = (Document<Base<?>>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public boolean isOpen() {
		return __view != null;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> getAllDocuments() {
		org.openntf.domino.ViewEntryCollection _col;
		_col = __view.getAllEntries();

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> result = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(_col);
		return result;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> getAllDocumentsByKey(Object key) {
		org.openntf.domino.DocumentCollection _col;
		_col = __view.getAllDocumentsByKey(key, true);

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> result = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(_col);
		return result;
	}

	@Override
	public View<org.openntf.domino.View> refresh() {
		__view.refresh();
		return this;
	}

	@Override
	public void delete() {
		if (__view != null) {
			__view.remove();
			__view = null;
		}
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> search(String query) {
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = search(query, 0); 
		return _iterator;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> search(String query, int max) {
		org.openntf.domino.View __temp = null;

		__temp = __view.getParent().getView(__view.getName());			
		__temp.FTSearch(query, max);

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(__temp);

		return _iterator;
	}

	@Override
	public View<org.openntf.domino.View> addColumn(String name, String value, boolean isSorted) {
		ViewColumn __col =  __view.createColumn(__view.getColumnCount(), name, value);
		__col.setSorted(isSorted);
		return null;
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
