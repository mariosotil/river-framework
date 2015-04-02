package org.riverframework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.riverframework.core.DefaultSession;

/**
 * This static class is used for load a River NoSQL module, and create and close core Session objects that controls that
 * modules.
 * 
 * The session objects are used to creates core Database, Document, etc. objects.
 * 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public class River {
	public final static String MODULE_LOTUS_DOMINO = "org.riverframework.module.lotus.domino";
	public final static String MODULE_ORG_OPENNTF_DOMINO = "org.riverframework.module.org.openntf.domino";
	public final static String MODULE_HAZELCAST = "org.riverframework.module.hazelcast";

	private static Map<String, Session> map = new HashMap<String, Session>();
	private final static River INSTANCE = new River();

	protected River() {
		// Exists only to defeat instantiation
	}

	/**
	 * @return a instance from this class, as a Singleton
	 */
	public static River getInstance() {
		return INSTANCE;
	}

	/**
	 * This method loads a module that wraps libraries as lotus.domino or org.openntf.domino and creates a core Session
	 * object. Its behavior will depend on how the module is implemented. Anyway, this method creates the session just
	 * one time and returns the same every time is called. To free the memory, resources, etc., it's necessary to call
	 * the close method at the end of the program or process.
	 * 
	 * @param module
	 *            the full name package. There are some consts defined as River.MODULE_LOTUS_DOMINO or
	 *            River.MODULE_ORG_OPENNTF_DOMINO to help
	 * @param parameters
	 *            their values and how to set them will depend exclusively on how the module is implemented. Check the
	 *            module documentation.
	 * @return
	 */
	public Session getSession(String module, Object... parameters) {
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
				throw new RiverException("The module '" + module + "' can not be loaded. If you are using an non-official module, " +
						"check the module name and its design. Check the CLASSPATH.");
			}

			Method method;
			try {
				method = clazzFactory.getDeclaredMethod("createSession", Object[].class);
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

	/**
	 * This method will call the Session close method to free resources, memory, etc. Also, it will remove it from its
	 * internal table.
	 * 
	 * @param module
	 */
	public void closeSession(String module) {
		Session session = map.get(module);
		if (session != null) {
			session.close();
			map.remove(module);
		}
	}

}
