package org.riverframework.core;

import java.lang.reflect.Constructor;
import java.util.UUID;

import org.riverframework.Database;
import org.riverframework.RiverException;
import org.riverframework.Session;

public class DefaultSession implements org.riverframework.Session {
	// public static final boolean USE_POOL = true;
	// public static final boolean DO_NOT_USE_POOL = false;

	public static final String PREFIX = "RIVER_";
	private final static Session INSTANCE = new DefaultSession();

	private org.riverframework.wrapper.Session _session = null;
	private UUID sessionUUID = null;

	protected DefaultSession() {
		// Exists only to defeat instantiation.
		sessionUUID = UUID.randomUUID();
	}

	public static Session getInstance() {
		return INSTANCE;
	}

	@Override
	public String getUUID() {
		return sessionUUID.toString();
	}

	@Override
	public Session setWrappedSession(org.riverframework.wrapper.Session s) {
		if (s == null)
			throw new RiverException("You can't set a null Lotus Notes session");
		_session = s;
		
		return this;
	}

	@Override
	public Session open(org.riverframework.wrapper.Session _s) {
		_session = _s;
		return INSTANCE;
	}

	@Override
	public boolean isOpen() {
		return (_session != null);
	}

	@Override
	public <U extends Database> U getDatabase(String... parameters) {
		return getDatabase(null, parameters);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.Database> U getDatabase(Class<U> clazz, String... location) {
		U database = null;
		Class<U> c = clazz;
		org.riverframework.wrapper.Database _database = null;

		if (c == null)
			c = (Class<U>) DefaultDatabase.class;

		if (!DefaultDatabase.class.isAssignableFrom(clazz))
			throw new RiverException("The clazz parameter must inherit from DefaultDatabase.");

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		if (_database == null || !_database.isOpen()) {
			_database = _session.getDatabase(server, path);
		}

		if (_database != null && !_database.isOpen()) {
			_database = null;
		}

		try {
			Constructor<?> constructor = c.getDeclaredConstructor(Session.class,
					org.riverframework.wrapper.Database.class);
			constructor.setAccessible(true);
			database = c.cast(constructor.newInstance(this, _database));
		} catch (Exception e) {
			throw new RiverException(e);
		}

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
