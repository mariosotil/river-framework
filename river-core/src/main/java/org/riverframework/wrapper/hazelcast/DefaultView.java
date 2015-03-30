package org.riverframework.wrapper.hazelcast;

import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentCollection;
import org.riverframework.wrapper.View;

class DefaultView implements org.riverframework.wrapper.View {
	public DefaultView(lotus.domino.View v) {
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
	public DocumentCollection getAllDocuments() {
		return null;
	}

	@Override
	public DocumentCollection getAllDocumentsByKey(Object key) {
		return null;
	}

	@Override
	public View refresh() {
		return this;
	}

	@Override
	public DocumentCollection search(String query) {
		return null;
	}
}
