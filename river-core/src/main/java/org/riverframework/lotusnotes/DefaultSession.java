package org.riverframework.lotusnotes;

import java.lang.reflect.Constructor;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;

public class DefaultSession implements org.riverframework.lotusnotes.Session {
	// public static final boolean USE_POOL = true;
	// public static final boolean DO_NOT_USE_POOL = false;

	public static final String PREFIX = "RIVER_";

	private final static Session INSTANCE = new DefaultSession();
	private static lotus.domino.Session session = null;

	protected DefaultSession() {
		// Exists only to defeat instantiation.
	}

	public static Session getInstance() {
		return INSTANCE;
	}

	public void setSession(lotus.domino.Session s) {
		if (s == null)
			throw new RiverException("You can't set a null Lotus Notes session");
		session = s;
	}

	@Override
	public Session open(String... parameters) {
		try {
			String server = "";
			String user = "";
			String password = "";

			switch (parameters.length) {
			case 1:
				server = parameters[0];
				session = NotesFactory.createSession(server);
				break;
			case 3:
				server = parameters[0];
				user = parameters[1];
				password = parameters[2];

				session = NotesFactory.createSession(server, user, password);
				break;
			default:
				session = null;
				break;
			}
		} catch (NotesException e) {
			throw new RiverException("There is a problem about the Session opening", e);
		}

		return INSTANCE;
	}

	@Override
	public void close() {
		try {
			if (session != null) {
				session.recycle();
				session = null;
			}
		} catch (NotesException e) {
			throw new RiverException("There is a problem with the getting a View map", e);
		}
	}

	@Override
	public boolean isOpen() {
		return (session != null);
	}

	@Override
	public lotus.domino.Session getNotesSession() {
		if (session == null)
			throw new RiverException("You can't get a null Lotus Notes session");
		return session;
	}

	@Override
	public <U extends org.riverframework.Database> U getDatabase(Class<U> clazz, String... parameters) {
		U rDatabase = null;

		if (clazz == null)
			throw new RiverException("The clazz parameter can not be null.");

		try {
			if (DefaultDatabase.class.isAssignableFrom(clazz)) {
				Constructor<?> constructor = clazz.getDeclaredConstructor(Session.class, String[].class);
				constructor.setAccessible(true);
				rDatabase = clazz.cast(constructor.newInstance(this, parameters));
			}

		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDatabase;
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}
}
