package org.riverframework.wrapper.openntf;

import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentCollection;
import org.riverframework.wrapper.View;

public class DefaultDatabase implements org.riverframework.wrapper.Database {
	protected org.openntf.domino.Database _database = null;

	protected DefaultDatabase(org.openntf.domino.Database obj) {
		_database = obj;
	}

	@Override
	public String getObjectId() {
		return _database.getReplicaID();
	}

	@Override
	public String getMainServer() {
		return _database.getACL().getAdministrationServer();
	}

	@Override
	public String getServer() {
		return _database.getServer();
	}

	@Override
	public String getFilePath() {
		return _database.getFilePath();
	}

	@Override
	public String getName() {
		return _database.getTitle();
	}

	@Override
	public boolean isOpen() {
		return (_database != null && _database.isOpen());
	}

	@Override
	public Document createDocument(String... parameters) {
		org.openntf.domino.Document _doc = null;

		_doc = _database.createDocument();

		Document doc = new DefaultDocument(_doc);
		return doc;
	}

	@Override
	public Document getDocument(String... parameters)
	{
		org.openntf.domino.Document _doc = null;

		if (parameters.length > 0) {
			String id = parameters[0];

			if (id.length() == 32) {
				_doc = _database.getDocumentByUNID(id);
			}

			if (_doc == null && id.length() == 8) {
				_doc = _database.getDocumentByID(id);
			}
		}

		Document doc = new DefaultDocument(_doc);
		return doc;
	}

	@Override
	public View getView(String... parameters) {
		org.openntf.domino.View _view = null;

		if (parameters.length > 0) {
			String id = parameters[0];
			_view = _database.getView(id);
		}

		if (_view != null)
			_view.setAutoUpdate(false);

		View view = new DefaultView(_view);
		return view;
	}

	@Override
	public DocumentCollection getAllDocuments() {
		org.openntf.domino.DocumentCollection _col;

		_col = _database.getAllDocuments();

		DocumentCollection col = new DefaultDocumentCollection(_col);

		return col;
	}

	@Override
	public DocumentCollection search(String query) {
		org.openntf.domino.DocumentCollection _col;

		_col = _database.FTSearch(query);
		DocumentCollection result = new DefaultDocumentCollection(_col);

		return result;
	}

	@Override
	public Database refreshSearchIndex() {
		_database.updateFTIndex(false);
		return this;
	}
}
