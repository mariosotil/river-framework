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
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.View;

class DefaultDatabase extends DefaultBase<org.openntf.domino.Database> implements org.riverframework.wrapper.Database<org.openntf.domino.Database> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;
	protected org.riverframework.wrapper.Session<org.openntf.domino.Session> _session = null;
	protected org.riverframework.wrapper.Factory<org.openntf.domino.Base<?>> _factory = null;
	protected volatile org.openntf.domino.Database __database = null;
	private String objectId = null;

	@SuppressWarnings("unchecked")
	protected DefaultDatabase(org.riverframework.wrapper.Session<org.openntf.domino.Session> _s, org.openntf.domino.Database __obj) {
		__database = __obj;
		_session = _s;
		_factory = (Factory<Base<?>>) _session.getFactory();
		objectId = calcObjectId(__database);
	}

	@Override
	public org.openntf.domino.Database getNativeObject() {
		return __database;
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	public static String calcObjectId(org.openntf.domino.Database __database) {
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
		return __database.getServer();
	}

	@Override
	public String getFilePath() {
		return __database.getFilePath();
	}

	@Override
	public String getName() {
		return __database.getTitle();
	}

	@Override
	public boolean isOpen() {
		return (__database != null && __database.isOpen());
	}

	@Override
	public Document<org.openntf.domino.Document> createDocument(String... parameters) {
		org.openntf.domino.Document __doc = null;

		__doc = __database.createDocument();

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
					__doc = __database.getDocumentByUNID(id);
				}
			} catch (Exception e) {
				// Maybe it was an invalid UNID. We just ignore the exception.
			}

			try {
				if (__doc == null && id.length() == 8) {
					__doc = __database.getDocumentByID(id);
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
			__view = __database.createView(name);
		} else if (parameters.length == 2) {
			name = parameters[0];
			String selectionFormula = parameters[1];
			__view = __database.createView(name, selectionFormula);
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
			__view = __database.getView(id);
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

		_col = __database.getAllDocuments();

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> search(String query) {
		org.openntf.domino.DocumentCollection _col;

		_col = __database.FTSearch(query);

		@SuppressWarnings("unchecked")
		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> _iterator = 
		(DocumentIterator<Base<?>, org.openntf.domino.Document>) _factory.getDocumentIterator(_col);
		return _iterator;
	}

	@Override
	public Database<org.openntf.domino.Database> refreshSearchIndex(boolean createIfNotExist) {
		__database.updateFTIndex(createIfNotExist);
		return this;
	}

	@Override
	public void delete() {
		if (__database != null) 
			__database.remove();			

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