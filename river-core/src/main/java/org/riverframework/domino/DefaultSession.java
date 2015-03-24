package org.riverframework.domino;

import java.lang.reflect.Constructor;
import java.util.UUID;

import lotus.domino.NotesFactory;

import org.openntf.domino.utils.Factory;
import org.riverframework.RiverException;

public class DefaultSession implements org.riverframework.domino.Session {
	// public static final boolean USE_POOL = true;
	// public static final boolean DO_NOT_USE_POOL = false;

	public static final String PREFIX = "RIVER_";
	private final static Session INSTANCE = new DefaultSession();
	
	private org.openntf.domino.Session _session = null;
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
	
	public void setSession(org.openntf.domino.Session s) {
		if (s == null)
			throw new RiverException("You can't set a null Lotus Notes session");
		_session = s;
	}

	@Override
	public Session open(String... parameters) {
		String username = "";
		String password = "";
		lotus.domino.Session s = null;

		try {
			switch (parameters.length) {
			case 0:
				s = NotesFactory.createSession();
				_session = Factory.fromLotus(s, org.openntf.domino.impl.Session.class, null);
				break;

			case 1:
				password = parameters[0];
				s = NotesFactory.createSession((String) null, (String) null, password);
				_session = Factory.fromLotus(s, org.openntf.domino.impl.Session.class, null);
				break;

			case 2:
				username = parameters[0];
				password = parameters[1];
				s = NotesFactory.createSession((String) null, username, password);
				_session = Factory.fromLotus(s, org.openntf.domino.impl.Session.class, null);
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
	public org.openntf.domino.DateTime Java2NotesTime(java.util.Date d) {
		org.openntf.domino.DateTime result = _session.createDateTime(d);
		return result;
	}

	@Override
	public <U extends org.riverframework.Database> U getDatabase(Class<U> clazz, String... location) {
		U rDatabase = null;
		org.openntf.domino.Database database = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		if (!DefaultDatabase.class.isAssignableFrom(clazz))
			throw new RiverException("The clazz parameter must inherit from DefaultDatabase.");

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		if (path.length() == 16) {
			database = _session.getDatabase(null, " ", true);
			database.openByReplicaID(server, path);
		}

		if (database == null || !database.isOpen()) {
			database = _session.getDatabase(server, path, false);
		}

		if (database != null && !database.isOpen()) {
			database = null;
		}

		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(Session.class, org.openntf.domino.Database.class);
			constructor.setAccessible(true);
			rDatabase = clazz.cast(constructor.newInstance(this, database));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDatabase;
	}

	@Override
	public String getUserName() {
		return _session.getUserName();
	}

	@Override
	public void close() {

	}
}
