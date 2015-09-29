package org.riverframework.wrapper.org.openntf.domino;

// import java.util.logging.Level;
// import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.openntf.domino.Base;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.View;

class DefaultDatabase extends AbstractBaseOrgOpenntfDomino<org.openntf.domino.Database> implements org.riverframework.wrapper.Database<org.openntf.domino.Database> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	protected DefaultDatabase(org.riverframework.wrapper.Session<org.openntf.domino.Session> _session, org.openntf.domino.Database __native) {
		super(_session, __native);
	}

	@Override
	public String calcObjectId(org.openntf.domino.Database __database) {
		String objectId = "";

		if (__database != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(__database.getServer());
			sb.append(River.ID_SEPARATOR);
			sb.append(__database.getFilePath());

			objectId = sb.toString();
		} 

		return objectId;
	}

	@Override
	public String getServer() {
		return __native.getServer();
	}

	@Override
	public String getFilePath() {
		return __native.getFilePath();
	}

	@Override
	public String getName() {
		return __native.getTitle();
	}

	@Override
	public boolean isOpen() {
		return (__native != null && __native.isOpen());
	}

	@Override
	public Document<org.openntf.domino.Document> createDocument(String... parameters) {
		org.openntf.domino.Document __doc = null;

		__doc = __native.createDocument();

		@SuppressWarnings("unchecked")
		Document<org.openntf.domino.Document> doc = (Document<org.openntf.domino.Document>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public Document<org.openntf.domino.Document> getDocument(String... parameters)
	{
		org.openntf.domino.Document __doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			String[] temp = id.split(Pattern.quote(River.ID_SEPARATOR));
			if (temp.length == 3) {
				id = temp[2];
			}

			try {
				if (id.length() == 32) {
					__doc = __native.getDocumentByUNID(id);
				}
			} catch (Exception e) {
				// Maybe it was an invalid UNID. We just ignore the exception.
			}

			try {
				if (__doc == null && id.length() == 8) {
					__doc = __native.getDocumentByID(id);
				}
			} catch (Exception e) {
				// Maybe it was an invalid UNID. We just ignore the exception.
			}
		}

		@SuppressWarnings("unchecked")
		Document<org.openntf.domino.Document> doc = (Document<org.openntf.domino.Document>) _factory.getDocument(__doc);

		return doc;
	}

	@Override
	public View<org.openntf.domino.View> createView(String... parameters) {
		org.openntf.domino.View __view = null;
		String name = null;

		if (parameters.length == 1) {
			name = parameters[0];
			__view = __native.createView(name);
		} else if (parameters.length == 2) {
			name = parameters[0];
			String selectionFormula = parameters[1];
			__view = __native.createView(name, selectionFormula);
		} else {
			throw new RiverException("It was expected these parameters: name (required) and selection formula (optional).");
		}			

		if (name != null && !name.equals("") && __view != null) {
			__view = null;
		}

		View<org.openntf.domino.View> _view = getView(name);
		return _view;
	}

	@Override
	public View<org.openntf.domino.View> getView(String... parameters) {
		org.openntf.domino.View __view = null;

		if (parameters.length > 0) {
			String id = parameters[0];
			__view = __native.getView(id);
		}

		if (__view != null)
			__view.setAutoUpdate(false);

		@SuppressWarnings("unchecked")
		View<org.openntf.domino.View> _view = (View<org.openntf.domino.View>) _factory.getView(__view);
		return _view;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> getAllDocuments() {
		org.openntf.domino.DocumentCollection _col;

		_col = __native.getAllDocuments();

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> search(String query) {
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = search(query, 0);
		return _iterator;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> search(String query, int max) {
		org.openntf.domino.DocumentCollection _col;

		org.openntf.domino.DateTime __date = _session.getNativeObject().createDateTime("1/1/0001");
		_col = __native.search(query, __date, max);

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public Database<org.openntf.domino.Database> refreshSearchIndex(boolean createIfNotExist) {
		__native.updateFTIndex(createIfNotExist);
		return this;
	}

	@Override
	public void delete() {
		if (__native != null) 
			__native.remove();			

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
