package org.riverframework.wrapper.hazelcast;

import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentCollection;
import org.riverframework.wrapper.View;

class DefaultDatabase implements org.riverframework.wrapper.Database {
	protected DefaultDatabase(lotus.domino.Database obj) {
	}

	@Override
	public Object getWrappedObject() {
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
