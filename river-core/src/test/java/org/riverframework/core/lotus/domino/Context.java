package org.riverframework.core.lotus.domino;

import org.riverframework.core.Credentials;
import org.riverframework.wrapper.SessionFactory;
import org.riverframework.wrapper.SessionModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "test-configuration-lotus-domino-local";
	}

	@Override
	public org.riverframework.wrapper.Session getSession() {
		Injector injector = Guice.createInjector(new SessionModule());
		SessionFactory sessionFactory = injector.getInstance(SessionFactory.class);

		org.riverframework.wrapper.Session _session = sessionFactory.createDomino(null, null, Credentials.getPassword());

		return _session;
	}
}
