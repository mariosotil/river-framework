package org.riverframework.wrapper.hazelcast;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentList;
import org.riverframework.wrapper.View;

class DefaultDatabase implements org.riverframework.wrapper.Database {
	com.hazelcast.core.IMap<String, Map<String, Object>> _database = null;
	
	protected DefaultDatabase(com.hazelcast.core.IMap<String, Map<String, Object>> obj) {
		_database = obj;
	}

	@Override
	public com.hazelcast.core.IMap<String, Map<String, Object>> getNativeObject() {
		return _database;
	}

	@Override
	public String getObjectId() {
		return _database.getName();
	}

	@Override
	public String getServer() {
		return "(Unknown)";
	}

	@Override
	public String getFilePath() {
		return "(Unknown)";
	}

	@Override
	public String getName() {
		return _database.getName();
	}

	@Override
	public boolean isOpen() {
		return _database != null;
	}

	@Override
	public Document createDocument(String... parameters) {
		String uuid = UUID.randomUUID().toString();
		
		Map<String, Object> _doc = new HashMap<String, Object>();
		_doc.put(DefaultDocument.FIELD_UUID, uuid);
		_database.put(uuid, _doc);
		
		Document doc = new DefaultDocument(_doc);
		try {
			Field newDocument = DefaultDocument.class.getDeclaredField("newDocument");
			newDocument.setAccessible(true);
			newDocument.set(doc, true);
		} catch (Exception e) {
			throw new RiverException(e);
		}
		return doc;
	}

	@Override
	public Document getDocument(String... parameters)
	{
		Map<String, Object> _doc = null;
		String uuid = "";
		
		if (parameters.length > 0) {
			uuid = parameters[0];
			_doc = _database.get(uuid);

			if (_doc == null) {
				_database.remove(uuid);
			} else if (!_doc.get(DefaultDocument.FIELD_UUID).equals(uuid)) {
				// Check that there's consistency between the UUID key and the UUID field
				_doc.put(DefaultDocument.FIELD_UUID, uuid);
			}
		}

		Document doc = new DefaultDocument(_doc);
		return doc;
	}

	@Override
	public View getView(String... parameters) {
		return null;
	}

	@Override
	public DocumentList getAllDocuments() {
		DocumentList col = new DefaultDocumentList(_database);
		return col;
	}

	@Override
	public DocumentList search(String query) {
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
