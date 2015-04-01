package org.riverframework.core.org.openntf.domino;

import org.riverframework.River;
import org.riverframework.Session;
import org.riverframework.core.Credentials;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-org-openntf-domino";
	}

	@Override
	public Session getSession() {
		Session session = River.getInstance().getSession(
				River.MODULE_ORG_OPENNTF_DOMINO,
				null, null, Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		River.getInstance().closeSession(River.MODULE_ORG_OPENNTF_DOMINO);
	}
}
