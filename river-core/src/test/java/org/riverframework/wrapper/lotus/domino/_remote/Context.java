package org.riverframework.wrapper.lotus.domino._remote;

import org.riverframework.Modules;
import org.riverframework.RiverFramework;
import org.riverframework.Session;
import org.riverframework.core.Credentials;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-remote";
	}

	@Override
	public Session getSession() {
		Session session = RiverFramework.getSession(
				Modules.MODULE_LOTUS_DOMINO,
				Credentials.getServer(),
				Credentials.getUsername(),
				Credentials.getPassword());
		return session;
	}

	@Override
	public void closeSession() {
		RiverFramework.closeSession(Modules.MODULE_LOTUS_DOMINO);
	}
}
