package org.riverframework.module.org.openntf.domino;

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
		Session session = River.getSession(
				River.MODULE_ORG_OPENNTF_DOMINO,
				(String) null, (String) null, Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.MODULE_ORG_OPENNTF_DOMINO);
	}
}
