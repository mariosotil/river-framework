package org.riverframework.core.lotus.domino._local;

import org.riverframework.River;
import org.riverframework.Session;
import org.riverframework.core.Credentials;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-local";
	}

	@Override
	public Session getSession() {
		Session session = River.getSession(
				River.MODULE_LOTUS_DOMINO,
				(String) null, (String) null, Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.MODULE_LOTUS_DOMINO);
	}
}
