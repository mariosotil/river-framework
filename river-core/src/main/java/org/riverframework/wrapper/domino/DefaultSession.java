package org.riverframework.wrapper.domino;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DefaultSession implements org.riverframework.wrapper.Session {
	private lotus.domino.Session _session = null;

	@Inject
	public DefaultSession(@Assisted String... parameters) {
		if (parameters.length != 3)
			throw new RiverException("There are needed three parameters: server, username and password.");

		String server = parameters[0];
		String username = parameters[1];
		String password = parameters[2];

		try {
			_session = NotesFactory.createSession(server, username, password);
		} catch (NotesException e) {
			_session = null;
			throw new RiverException(e);
		}
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

	}
}
