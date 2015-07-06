package org.riverframework.core;

import java.lang.reflect.Constructor;

import org.riverframework.River;
import org.riverframework.RiverException;

/**
 * It is used to manage a session using a NoSQL wrapper from this framework.
 * Allows access to databases.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultSession implements org.riverframework.core.Session {
	public static final String PREFIX = "RIVER_";

	private org.riverframework.wrapper.Session<?> _session = null;

	protected DefaultSession(org.riverframework.wrapper.Session<?> _s) {
		// Exists only to defeat instantiation.
		_session = _s;
	}

	@Override
	public String getObjectId() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Session object is closed.");

		return _session.getObjectId();
	}

	@Override
	public org.riverframework.wrapper.Session<?> getWrapperObject() {
		return _session;
	}

	@Override
	public boolean isOpen() {
		return (_session != null && _session.isOpen());
	}

	@Override
	public org.riverframework.core.Database createDatabase(String... location) {
		org.riverframework.wrapper.Database<?> _database = _session.createDatabase(location);
		org.riverframework.core.Database database = new DefaultDatabase(this, _database);

		return database;
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDatabase<?>> U createDatabase(Class<U> clazz, String... location) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Session object is closed.");

		org.riverframework.core.Database database = createDatabase(location);
		U xDatabase = null;

		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(org.riverframework.core.Database.class);
			constructor.setAccessible(true);
			xDatabase = clazz.cast(constructor.newInstance(database));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return xDatabase;
	}

	@Override
	public org.riverframework.core.Database getDatabase(String... location) {
		org.riverframework.wrapper.Database<?> _database = _session.getDatabase(location);
		org.riverframework.core.Database database = new DefaultDatabase(this, _database);

		return database;
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDatabase<?>> U getDatabase(Class<U> clazz, String... location) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Session object is closed.");

		U xDatabase = null;
		Database database = getDatabase(location);

		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(org.riverframework.core.Database.class);
			constructor.setAccessible(true);
			xDatabase = clazz.cast(constructor.newInstance(database));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return xDatabase;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void cleanUp() {
		_session.getFactory().cleanUp();
	}

	@Override
	public String getUserName() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Session object is closed.");

		return _session.getUserName();
	}

	/**
	 * It's the really responsible to close the session. It's called by the
	 * close() method. It's hide from the real world as a protected method,
	 * because it never has to be called alone. Only the River factory class can
	 * call it.
	 */
	protected void protectedClose() {
		_session.close();
	}

	@Override
	public void close() {
		// This is for prevent that the session be closed but not purged from
		// the River factory class.
		String wrapper = _session.getClass().getPackage().getName();
		River.closeSession(wrapper);
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + getWrapperObject().toString() + ")";
	}
}
