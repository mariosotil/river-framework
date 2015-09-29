package org.riverframework.wrapper.org.openntf.domino;

import org.openntf.domino.Base;
import org.openntf.domino.ViewColumn;
import org.riverframework.River;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultView extends AbstractBaseOrgOpenntfDomino<org.openntf.domino.View> implements org.riverframework.wrapper.View<org.openntf.domino.View> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	protected DefaultView(org.riverframework.wrapper.Session<org.openntf.domino.Session> _session, org.openntf.domino.View __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(org.openntf.domino.View __view) {
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
	public Document<org.openntf.domino.Base<?>> getDocumentByKey(String key) {
		org.openntf.domino.Document __doc = null;

		__doc = __native.getDocumentByKey(key, true);

		@SuppressWarnings("unchecked")
		Document<org.openntf.domino.Base<?>> doc = (Document<Base<?>>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public boolean isOpen() {
		return __native != null;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> getAllDocuments() {
		org.openntf.domino.ViewEntryCollection __col;
		__col = __native.getAllEntries();

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> result = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(__col);
		return result;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> getAllDocumentsByKey(Object key) {
		org.openntf.domino.DocumentCollection _col;
		_col = __native.getAllDocumentsByKey(key, true);

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> result = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(_col);
		return result;
	}

	@Override
	public View<org.openntf.domino.View> refresh() {
		__native.refresh();
		return this;
	}

	@Override
	public void delete() {
		if (__native != null) {
			__native.remove();
			__native = null;
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

		__temp = __native.getParent().getView(__native.getName());			
		__temp.FTSearch(query, max);

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(__temp);

		return _iterator;
	}

	@Override
	public View<org.openntf.domino.View> addColumn(String name, String value, boolean isSorted) {
		ViewColumn __col =  __native.createColumn(__native.getColumnCount(), name, value);
		__col.setSorted(isSorted);
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
