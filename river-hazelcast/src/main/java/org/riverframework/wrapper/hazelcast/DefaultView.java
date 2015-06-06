package org.riverframework.wrapper.hazelcast;

import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentList;
import org.riverframework.wrapper.View;

class DefaultView implements org.riverframework.wrapper.View {
	public DefaultView(Object v) {
	}

	@Override
	public Object getNativeObject() {
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
