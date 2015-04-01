package org.riverframework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.riverframework.core.DefaultSession;

public class River {
	public final static String MODULE_LOTUS_DOMINO = "org.riverframework.module.lotus.domino";
	public final static String MODULE_ORG_OPENNTF_DOMINO = "org.riverframework.module.org.openntf.domino";
	public final static String MODULE_HAZELCAST = "org.riverframework.module.hazelcast";

	private static Map<String, Session> map = new HashMap<String, Session>();
	private final static River INSTANCE = new River();

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

			Class<?> clazzFactory = null;

			try {
				clazzFactory = Class.forName(module + ".Factory");
			} catch (ClassNotFoundException e1) {
				throw new RiverException("The module '" + module + "' is not loaded. Check the CLASSPATH.");
			}

			Method method;
			try {
				method = clazzFactory.getDeclaredMethod("createSession", String[].class);
				method.setAccessible(true);
				_session = (org.riverframework.module.Session) method.invoke(null, new Object[] { parameters });

				Constructor<?> constructor = DefaultSession.class.getDeclaredConstructor(org.riverframework.module.Session.class);
				constructor.setAccessible(true);
				session = (DefaultSession) constructor.newInstance(_session);
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
