package org.riverframework.domino;

import java.lang.reflect.Constructor;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.openntf.domino.utils.Factory;
import org.riverframework.RiverException;

public class DefaultSession implements org.riverframework.domino.Session {
	// public static final boolean USE_POOL = true;
	// public static final boolean DO_NOT_USE_POOL = false;

	public static final String PREFIX = "RIVER_";

	private final static Session INSTANCE = new DefaultSession();
	private static org.openntf.domino.Session session = null;

	protected DefaultSession() {
		// Exists only to defeat instantiation.
	}

	public static Session getInstance() {
		return INSTANCE;
	}

	public void setSession(org.openntf.domino.Session s) {
		if (s == null)
			throw new RiverException("You can't set a null Lotus Notes session");
		session = s;
	}

	@Override
	public Session open(String... parameters) {
		try {
			String password = "";
			lotus.domino.Session s = null;

			switch (parameters.length) {
			case 0:
				s = NotesFactory.createSession();
				session = Factory.fromLotus(s, org.openntf.domino.impl.Session.class, null);
				break;

			case 1:
				password = parameters[0];
				s = NotesFactory.createSession((String) null, (String) null, password);
				session = Factory.fromLotus(s, org.openntf.domino.impl.Session.class, null);
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
	public boolean isOpen() {
		return (session != null);
	}

	@Override
	public org.openntf.domino.Session getNotesSession() {
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
	public void close() {

	}
}
