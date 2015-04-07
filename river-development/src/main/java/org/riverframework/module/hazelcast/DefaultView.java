package org.riverframework.module.hazelcast;

import org.riverframework.module.Document;
import org.riverframework.module.DocumentCollection;
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

	@Override
	public void close() {
	}
}
