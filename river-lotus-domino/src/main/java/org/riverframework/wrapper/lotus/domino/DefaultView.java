package org.riverframework.wrapper.lotus.domino;

import lotus.domino.Base;
import lotus.domino.NotesException;
import lotus.domino.ViewColumn;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultView extends AbstractBaseLotusDomino<lotus.domino.View> implements org.riverframework.wrapper.View<lotus.domino.View> {
	protected DefaultView(org.riverframework.wrapper.Session<lotus.domino.Session> _session, lotus.domino.View __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(lotus.domino.View __view) {
		String objectId = "";

		if (__view != null) { // && !isRecycled(__view)) {
			try {
				lotus.domino.Database __database = __view.getParent();

				StringBuilder sb = new StringBuilder(1024);
				sb.append(__database.getServer());
				sb.append(River.ID_SEPARATOR);
				sb.append(__database.getFilePath());
				sb.append(River.ID_SEPARATOR);
				sb.append(__view.getName());

				objectId = sb.toString();
			} catch (NotesException e) {
				throw new RiverException(e);
			}	
		}

		return objectId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document<lotus.domino.Document> getDocumentByKey(String key) {
		lotus.domino.Document __doc = null;

		try {
			__doc = __native.getDocumentByKey(key, true);
			while (__doc != null && __doc.isDeleted()) 
			{
				// TODO: register this documents into the cache to avoid the conflict after recycling
				// lotus.domino.Document __deleted = __doc;
				__doc = __native.getNextDocument(__doc);
				// __deleted.recycle();  <== Bad idea
			}

			if (__doc != null && !__doc.getColumnValues().get(0).equals(key)) __doc = null;

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Document<lotus.domino.Document> doc = (Document<lotus.domino.Document>) _factory.getDocument(__doc);

		return doc;
	}

	public boolean isRecycled() {
		return isObjectRecycled(__native);
	}

	@Override
	public boolean isOpen() {
		return __native != null && !isRecycled();
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> getAllDocuments() {
		lotus.domino.ViewEntryCollection __vecol;
		try {
			__vecol = __native.getAllEntries();

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> result = (DocumentIterator<Base, lotus.domino.Document>) _factory.getDocumentIterator(__vecol);
		return result;
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> getAllDocumentsByKey(Object key) {
		lotus.domino.DocumentCollection _col;
		try {
			_col = __native.getAllDocumentsByKey(key, true);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> result = (DocumentIterator<Base, lotus.domino.Document>) _factory.getDocumentIterator(_col);
		return result;
	}

	@Override
	public View<lotus.domino.View> refresh() {
		try {
			__native.refresh();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public View<lotus.domino.View> addColumn(String name, String value, boolean isSorted) {
		try {
			ViewColumn __col = __native.createColumn(__native.getColumnCount(), name, value);
			__col.setSorted(isSorted);

			if(_factory.getIsRemoteSession()) {
				// If this is a remote session, calling recycle permits to save
				// the changes. Otherwise, the view looks damaged.

				lotus.domino.Database __db = __native.getParent();
				String viewName = __native.getName();

				__native.recycle(); 
				__native = __db.getView(viewName);
			}
		} catch (NotesException e) {
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
			} catch (NotesException e) {
				throw new RiverException(e);
			} finally {
				__native = null;
			}
		}
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> search(String query) {
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = search(query, 0);

		return _iterator;
	}

	@Override
	public DocumentIterator<lotus.domino.Base, lotus.domino.Document> search(String query, int max) {
		lotus.domino.View __temp = null;

		try {
			__temp = __native.getParent().getView(__native.getName());			
			__temp.FTSearch(query, max);
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = (DocumentIterator<Base, lotus.domino.Document>) _factory.getDocumentIterator(__temp);

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
