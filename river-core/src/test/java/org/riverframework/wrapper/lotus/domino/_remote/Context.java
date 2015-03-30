package org.riverframework.wrapper.lotus.domino._remote;

import org.riverframework.core.Credentials;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.SessionFactory;
import org.riverframework.wrapper.SessionModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public Session getSession() {
		org.riverframework.wrapper.Session _session = null;
		Injector injector = Guice.createInjector(new SessionModule());
		SessionFactory sessionFactory = injector.getInstance(SessionFactory.class);

		_session = sessionFactory.createDomino(
				Credentials.getServer(),
				Credentials.getUsername(),
				Credentials.getPassword()
				);

		return _session;
	}

	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-remote";
	}
}
