package org.riverframework.module.lotus.domino;

import lotus.domino.NotesException;

import org.riverframework.RiverException;
import org.riverframework.module.Database;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DefaultSession implements org.riverframework.module.Session {
	private lotus.domino.Session _session = null;

	@Inject
	public DefaultSession(@Assisted lotus.domino.Session obj) {
		_session = obj;
	}

	@Override
	public Object getReferencedObject() {
		return _session;
	}

	@Override
	public boolean isOpen() {
		return (_session != null);
	}

	@Override
	public Database getDatabase(String... location) {
		lotus.domino.Database _database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		try {
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
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Database database = new DefaultDatabase(_database);
		return database;
	}

	@Override
	public String getUserName() {
		try {
			return _session.getUserName();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public void close() {
		try {
			if (_session != null)
				_session.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			_session = null;
		}
	}
}
