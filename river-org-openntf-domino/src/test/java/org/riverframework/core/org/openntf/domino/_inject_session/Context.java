package org.riverframework.core.org.openntf.domino._inject_session;

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
		org.openntf.domino.Session _session = null;

		try {
			_session = (org.openntf.domino.Session) org.openntf.domino.utils.Factory
					.fromLotus(NotesFactory.createSession((String) null, (String) null, Credentials.getPassword()),
							org.openntf.domino.Session.class, null);

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Session session = River.getSession(River.ORG_OPENNTF_DOMINO, _session);
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.ORG_OPENNTF_DOMINO);
	}
}
