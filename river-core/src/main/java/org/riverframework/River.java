package org.riverframework;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.riverframework.core.DefaultSession;
import org.riverframework.module.SessionFactory;
import org.riverframework.module.SessionModule;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class River {
	public final static String MODULE_LOTUS_DOMINO = "RiverModuleLotus";
	public final static String MODULE_ORG_OPENNTF_DOMINO = "RiverModuleOpenntf";
	public final static String MODULE_HAZELCAST = "RiverModuleHazelcast";

	private static Map<String, Session> map = new HashMap<String, Session>();
	private final static River INSTANCE = Guice.createInjector(new SessionModule()).getInstance(River.class);

	@Inject
	private SessionFactory sessionFactory;

	protected River() {
		// Exists only to defeat instantiation
	}

	public static River getInstance() {
		return INSTANCE;
	}

	public Session getSession(String module, String... parameters) {
		// This will be the session loaded depending the selected module
		org.riverframework.module.Session _session = null;

		// Trying to retrieve the session from the map
		Session session = map.get(module);

		if (session == null) {
			// If not exists, we create it using the factory

			if (module.equals(MODULE_LOTUS_DOMINO)) {
				_session = sessionFactory.create(org.riverframework.module.lotus.domino.Factory.createSession(parameters));
			} else if (module.equals(MODULE_ORG_OPENNTF_DOMINO)) {
				_session = sessionFactory.create(org.riverframework.module.org.openntf.domino.Factory.createSession(parameters));
			} else if (module.equals(MODULE_HAZELCAST)) {
				_session = sessionFactory.create(org.riverframework.module.hazelcast.Factory.createSession(parameters));
			}

			try {
				Class<? extends Session> c = DefaultSession.class;
				Constructor<?> constructor = c.getDeclaredConstructor(org.riverframework.module.Session.class);
				constructor.setAccessible(true);
				session = c.cast(constructor.newInstance(_session));
			} catch (Exception e) {
				throw new RiverException(e);
			}

			map.put(module, session);
		}

		return session;
	}

	public void closeSession(String module) {
		map.remove(module);
	}

}
