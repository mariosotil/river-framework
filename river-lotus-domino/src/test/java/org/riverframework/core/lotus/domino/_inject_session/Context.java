package org.riverframework.core.lotus.domino._inject_session;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.core.Session;
import org.riverframework.utils.Credentials;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-local";
	}

	@Override
	public Session getSession() {
		lotus.domino.Session _session = null;

		try {
			_session = NotesFactory.createSession((String) null, (String) null, Credentials.getPassword());
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Session session = River.getSession(River.LOTUS_DOMINO, _session);
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.LOTUS_DOMINO);
	}
}
