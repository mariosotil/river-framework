package org.riverframework.core.org.openntf.domino._local;

import org.riverframework.River;
import org.riverframework.core.Session;
import org.riverframework.utils.Credentials;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-local";
	}

	@Override
	public Session getSession() {
		Session session = River.getSession(
				River.ORG_OPENNTF_DOMINO,
				(String) null, (String) null, Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.ORG_OPENNTF_DOMINO);
	}
}
