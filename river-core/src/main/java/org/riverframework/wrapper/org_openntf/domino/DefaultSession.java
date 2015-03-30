package org.riverframework.wrapper.org_openntf.domino;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DefaultSession implements org.riverframework.wrapper.Session {
	private org.openntf.domino.Session _session = null;

	@Inject
	public DefaultSession(@Assisted String... parameters) {
		if (parameters.length != 3)
			throw new RiverException("There are needed three parameters: server, username and password.");

		String server = parameters[0];
		String username = parameters[1];
		String password = parameters[2];

		try {
			_session = Factory.fromLotus(NotesFactory.createSession(server, username, password), Session.class, null);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
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

	}
}
