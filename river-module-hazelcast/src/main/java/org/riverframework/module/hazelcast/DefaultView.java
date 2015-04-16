package org.riverframework.module.hazelcast;

import org.riverframework.module.Document;
import org.riverframework.module.DocumentList;
import org.riverframework.module.View;

class DefaultView implements org.riverframework.module.View {
	public DefaultView(Object v) {
	}

	@Override
	public Object getReferencedObject() {
		return null;
	}

	@Override
	public Document getDocumentByKey(String key) {
		return null;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public View refresh() {
		return this;
	}

	@Override
	public void close() {
	}

	@Override
	public String getObjectId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentList getAllDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentList getAllDocumentsByKey(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentList search(String query) {
		// TODO Auto-generated method stub
		return null;
	}
}
