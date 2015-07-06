package org.riverframework.core.org.openntf.domino._remote;

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
				River.ORG_OPENNTF_DOMINO,
				Credentials.getServer(),
				Credentials.getUsername(),
				Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.ORG_OPENNTF_DOMINO);
	}
}
