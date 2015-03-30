package org.riverframework;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.riverframework.core.DefaultSession;
import org.riverframework.wrapper.SessionFactory;
import org.riverframework.wrapper.SessionModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class RiverFramework {
	private static Map<Modules, Session> map = new HashMap<Modules, Session>();
	// private static Logger logger = Logger.getRootLogger();
	private static Injector injector = null;
	private static SessionFactory sessionFactory = null;

	static {
		// Loading the Session Factory
		injector = Guice.createInjector(new SessionModule());
		sessionFactory = injector.getInstance(SessionFactory.class);
	}

	protected RiverFramework() {
		// Exists only to defeat instantiation
	}

	public static Session getSession(Modules module, String... parameters) {
		// This will be the session loaded depending the selected module
		org.riverframework.wrapper.Session _session = null;

		// Trying to retrieve the session from the map
		Session session = map.get(module);

		if (session == null) {
			// If not exists, we create it using the factory
			switch (module) {
			case MODULE_LOTUS_DOMINO:
				_session = sessionFactory.createDomino(parameters[0], parameters[1], parameters[2]);
				break;
			case MODULE_ORG_OPENNTF_DOMINO:
				_session = sessionFactory.createOpenntf(parameters[0], parameters[1], parameters[2]);
				break;
			case MODULE_HAZELCAST:
				// TODO: create the Hazelcast session
				break;
			}

			try {
				Class<? extends Session> c = DefaultSession.class;
				Constructor<?> constructor = c.getDeclaredConstructor(org.riverframework.wrapper.Session.class);
				constructor.setAccessible(true);
				session = c.cast(constructor.newInstance(_session));
			} catch (Exception e) {
				throw new RiverException(e);
			}

			map.put(module, session);
			// logger.info("created singleton: " + session);
		} else {
			// If exists...
			// logger.info("got singleton from map: " + session);
		}

		return session;
	}

	public static void closeSession(Modules module) {
		map.remove(module);
	}

}
