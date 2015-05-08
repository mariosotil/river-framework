package org.riverframework.wrapper.hazelcast;

import java.util.ArrayList;
import java.util.Map;

import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.DocumentList;
import org.riverframework.wrapper.hazelcast.DefaultDocument;

class DefaultDocumentList extends ArrayList<Document> implements DocumentList {
	private static final long serialVersionUID = -5133904585806297966L;

	public DefaultDocumentList(com.hazelcast.core.IMap<String, Map<String, Object>> _database) {
		clear();
		for(String key : _database.keySet()) {
			Map<String, Object> _doc = _database.get(key);
			if (_doc != null) {
				Document doc = new DefaultDocument(_doc);
				this.add(doc);	
			}
		}
	}

	@Override
	public DocumentIterator iterator() {
		return null;
	}
	
	@Override
	public DocumentList deleteAll() {
		for (Document doc : this) {
			doc.delete();
		}
		return this;
	}

}
