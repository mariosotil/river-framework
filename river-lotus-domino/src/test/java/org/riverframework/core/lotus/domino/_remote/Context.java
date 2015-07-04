package org.riverframework.core.lotus.domino._remote;

import org.riverframework.River;
import org.riverframework.core.Session;
import org.riverframework.utils.Credentials;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-remote";
	}

	@Override
	public Session getSession() {
		Session session = River.getSession(
				River.LOTUS_DOMINO,
				Credentials.getServer(),
				Credentials.getUsername(),
				Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.LOTUS_DOMINO);
	}
}
