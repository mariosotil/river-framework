package org.riverframework.wrapper.lotus.domino;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultView extends DefaultBase implements org.riverframework.wrapper.View<org.openntf.domino.Base> {
	// private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected org.riverframework.wrapper.Session<org.openntf.domino.Base> _session = null;
	protected volatile org.openntf.domino.View __view = null;
	private String objectId = null;

	protected DefaultView(org.riverframework.wrapper.Session<org.openntf.domino.Base> s, org.openntf.domino.View v) {
		__view = v;
		_session = s;
		synchronized (_session){
			objectId = calcObjectId(__view);
		}		
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
	public Document<org.openntf.domino.Base> getDocumentByKey(String key) {
		synchronized (_session){
			org.openntf.domino.Document __doc = null;

			__doc = __view.getDocumentByKey(key, true);

			@SuppressWarnings("unchecked")
			Document<org.openntf.domino.Base> doc = _session.getFactory().getDocument(__doc);

			return doc;
		}
	}

	@Override
	public boolean isOpen() {
		return __view != null;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base> getAllDocuments() {
		synchronized (_session){
			org.openntf.domino.ViewEntryCollection _col;
			_col = __view.getAllEntries();

			@SuppressWarnings("unchecked")
			DocumentIterator<org.openntf.domino.Base> result = _session.getFactory().getDocumentIterator(_col);
			return result;
		}
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base> getAllDocumentsByKey(Object key) {
		synchronized (_session){
			org.openntf.domino.DocumentCollection _col;
			_col = __view.getAllDocumentsByKey(key, true);

			@SuppressWarnings("unchecked")
			DocumentIterator<org.openntf.domino.Base> result = _session.getFactory().getDocumentIterator(_col);
			return result;
		}
	}

	@Override
	public View<org.openntf.domino.Base> refresh() {
		synchronized (_session){
			__view.refresh();
		}
		return this;
	}

	@Override
	public void delete() {
		synchronized (_session){
			if (__view != null) {
				__view.remove();
				__view = null;
			}
		}
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base> search(String query) {
		synchronized (_session){
			org.openntf.domino.View __temp = null;

			__temp = __view.getParent().getView(__view.getName());			
			__temp.FTSearch(query);

			@SuppressWarnings("unchecked")
			DocumentIterator<org.openntf.domino.Base> _iterator = _session.getFactory().getDocumentIterator(__temp);

			return _iterator;
		}
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
