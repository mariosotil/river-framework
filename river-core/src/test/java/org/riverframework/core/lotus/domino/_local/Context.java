package org.riverframework.core.lotus.domino._local;

import org.riverframework.Modules;
import org.riverframework.RiverFramework;
import org.riverframework.Session;
import org.riverframework.core.Credentials;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-local";
	}

	@Override
	public Session getSession() {
		Session session = RiverFramework.getSession(
				Modules.MODULE_LOTUS_DOMINO,
				null, null, Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		RiverFramework.closeSession(Modules.MODULE_LOTUS_DOMINO);
	}
}
