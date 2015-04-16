package org.riverframework.module.hazelcast;

import java.util.ArrayList;
import java.util.Map;

import org.riverframework.module.Document;
import org.riverframework.module.DocumentList;
import org.riverframework.module.hazelcast.DefaultDocument;

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
	public DocumentList deleteAll() {
		for (Document doc : this) {
			doc.delete();
		}
		return this;
	}

}
