package org.riverframework.module.hazelcast;

import java.util.ArrayList;

import org.riverframework.module.Document;
import org.riverframework.module.DocumentCollection;

class DefaultDocumentCollection extends ArrayList<Document> implements DocumentCollection {
	private static final long serialVersionUID = -5133904585806297966L;

	public DefaultDocumentCollection(lotus.domino.DocumentCollection _col) {
	}

	public DefaultDocumentCollection(lotus.domino.View _view) {
	}

	public DefaultDocumentCollection(lotus.domino.ViewEntryCollection _col) {
	}

	@Override
	public DocumentCollection deleteAll() {
		return this;
	}

}
