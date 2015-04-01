package org.riverframework.module.hazelcast;

import org.riverframework.module.Database;
import org.riverframework.module.Document;
import org.riverframework.module.DocumentCollection;
import org.riverframework.module.View;

class DefaultDatabase implements org.riverframework.module.Database {
	protected DefaultDatabase(lotus.domino.Database obj) {
	}

	@Override
	public Object getReferencedObject() {
		return null;
	}

	@Override
	public String getObjectId() {
		return "";
	}

	@Override
	public String getServer() {
		return "";
	}

	@Override
	public String getFilePath() {
		return "";
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public Document createDocument(String... parameters) {
		return null;
	}

	@Override
	public Document getDocument(String... parameters)
	{
		return null;
	}

	@Override
	public View getView(String... parameters) {
		return null;
	}

	@Override
	public DocumentCollection getAllDocuments() {
		return null;
	}

	@Override
	public DocumentCollection search(String query) {
		return null;
	}

	@Override
	public Database refreshSearchIndex() {
		return null;
	}

	@Override
	public void close() {
	}
}
