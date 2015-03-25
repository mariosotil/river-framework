package org.riverframework.domino;

import java.lang.reflect.Constructor;
import java.util.UUID;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;

public class DefaultSession implements org.riverframework.domino.Session {
	// public static final boolean USE_POOL = true;
	// public static final boolean DO_NOT_USE_POOL = false;

	public static final String PREFIX = "RIVER_";
	private final static Session INSTANCE = new DefaultSession();

	private lotus.domino.Session _session = null;
	private UUID sessionUUID = null;

	protected DefaultSession() {
		// Exists only to defeat instantiation.
		sessionUUID = UUID.randomUUID();
	}

	public static Session getInstance() {
		return INSTANCE;
	}

	public String getUUID() {
		return sessionUUID.toString();
	}

	public void setSession(lotus.domino.Session s) {
		if (s == null)
			throw new RiverException("You can't set a null Lotus Notes session");
		_session = s;
	}

	@Override
	public Session open(String... parameters) {
		String username = "";
		String password = "";

		try {
			switch (parameters.length) {
			case 0:
				_session = NotesFactory.createSession();
				break;

			case 1:
				password = parameters[0];
				_session = NotesFactory.createSession((String) null, (String) null, password);
				break;

			case 2:
				username = parameters[0];
				password = parameters[1];
				_session = NotesFactory.createSession((String) null, username, password);
				break;

			default:
				_session = null;
				break;
			}
		} catch (Exception e) {
			throw new RiverException("There is a problem opening the Session ", e);
		}

		return INSTANCE;
	}

	@Override
	public boolean isOpen() {
		return (_session != null);
	}

	@Override
	public lotus.domino.DateTime Java2NotesTime(java.util.Date d) {
		lotus.domino.DateTime result;
		try {
			result = _session.createDateTime(d);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
		return result;
	}

	@Override
	public <U extends org.riverframework.Database> U getDatabase(Class<U> clazz, String... location) {
		U rDatabase = null;
		lotus.domino.Database database = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (!DefaultDatabase.class.isAssignableFrom(clazz))
			throw new RiverException("The clazz parameter must inherit from DefaultDatabase.");

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		//		2015.03.25 - This feature does not work in the version 4 of Openntf Domino API
		//		if (path.length() == 16) {
		//			database = _session.getDatabase(null, null);
		//			boolean res = database.openByReplicaID(server, path);
		//			if (!res) database = null;
		//		}

		try {
			if (database == null || !database.isOpen()) {
				database = _session.getDatabase(server, path, false);
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		try {
			if (database != null && !database.isOpen()) {
				database = null;
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(Session.class, lotus.domino.Database.class);
			constructor.setAccessible(true);
			rDatabase = clazz.cast(constructor.newInstance(this, database));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDatabase;
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
