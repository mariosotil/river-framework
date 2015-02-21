package org.riverframework.lotusnotes;

import java.lang.reflect.Constructor;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.fw.AbstractSession;

public class DefaultSession extends AbstractSession {
	private final static DefaultSession INSTANCE = new DefaultSession();
	private static lotus.domino.Session session = null;

	protected DefaultSession() {
		// Exists only to defeat instantiation.
	}

	public static DefaultSession getInstance() {
		return INSTANCE;
	}

	public void setSession(lotus.domino.Session s) {
		if (s == null)
			throw new RiverException("You can't set a null Lotus Notes session");
		session = s;
	}

	@Override
	public DefaultSession open(String... parameters) {
		try {
			if (parameters.length == 0) {
				session = NotesFactory.createSession();

			} else {
				String server = parameters[0];
				String user = parameters[1];
				String password = parameters[2];

				session = NotesFactory.createSession(server, user, password);
			}
		} catch (NotesException e) {
			throw new RiverException("There is a problem with the closing of the Context", e);
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
			throw new RiverException("There is a problem with the closing of the Context", e);
		}
	}

	@Override
	public boolean isOpen() {
		return (session != null);
	}

	public lotus.domino.Session getNotesSession() {
		if (session == null)
			throw new RiverException("You can't get a null Lotus Notes session");
		return session;
	}

	@Override
	public <U extends org.riverframework.Database<?>> U getDatabase(Class<U> type, String... parameters) {
		U rDatabase = null;

		try {
			if (type != null) {
				Constructor<?> constructor = type.getConstructor();
				rDatabase = type.cast(constructor.newInstance());
				type.cast(rDatabase).open(parameters);
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
