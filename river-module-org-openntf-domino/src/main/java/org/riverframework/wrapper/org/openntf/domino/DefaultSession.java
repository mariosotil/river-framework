package org.riverframework.wrapper.org.openntf.domino;

import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Factory;

public class DefaultSession implements org.riverframework.wrapper.Session {
	private org.openntf.domino.Session _session = null;
	private UUID sessionUUID = null;

	public DefaultSession(org.openntf.domino.Session obj) {
		_session = obj;
		sessionUUID = UUID.randomUUID();
	}

	@Override
	public String getObjectId() {
		return sessionUUID.toString();
	}
	
	@Override
	public org.openntf.domino.Session getNativeObject() {
		return _session;
	}

	@Override
	public boolean isOpen() {
		return (_session != null);
	}

	@Override
	public Database getDatabase(String... location) {
		org.openntf.domino.Database _database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		if (path.length() == 16) {
			boolean res = false;
			_database = _session.getDatabase(null, null);
			res = _database.openByReplicaID(server, path);
			if (!res)
				_database = null;
		}

		if (_database == null || !_database.isOpen()) {
			_database = _session.getDatabase(server, path, false);
		}

		if (_database != null && !_database.isOpen()) {
			_database = null;
		}

		Database database = new DefaultDatabase(_database);
		return database;
	}

	@Override
	public String getUserName() {
		return _session.getUserName();
	}

	@Override
	public void close() {
		_session = null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Factory getFactory() {
		// TODO Auto-generated method stub
		return null;
	}
}
