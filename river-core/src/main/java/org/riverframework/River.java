package org.riverframework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.riverframework.core.AbstractSession;
import org.riverframework.core.DefaultSession;

/**
 * This static class is used for load a River NoSQL module, and create and close core Session objects for control the
 * data.
 * 
 * The session objects are used for create the Database, Document, View and DocumentList objects.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public class River {
	public final static String MODULE_LOTUS_DOMINO = "org.riverframework.module.lotus.domino";
	public final static String MODULE_ORG_OPENNTF_DOMINO = "org.riverframework.module.org.openntf.domino";

	private final static Map<String, AbstractSession> map = new HashMap<String, AbstractSession>();

	// private final static River INSTANCE = new River();

	private River() {
		// Exists only to defeat instantiation
	}

	/**
	 * @return a instance from this class, as a Singleton
	 */
	// public static River getInstance() {
	// return INSTANCE;
	// }

	/**
	 * Loads a module that wraps libraries as lotus.domino or org.openntf.domino and creates a core Session
	 * object. Its behavior will depend on how the module is implemented. Anyway, this method creates the session just
	 * one time and returns the same every time is called. To free the memory, resources, etc., it's necessary to call
	 * the close method at the end of the program or process.
	 * 
	 * @param module
	 *            the package's full name. You can use the constants defined as River.MODULE_LOTUS_DOMINO or
	 *            River.MODULE_ORG_OPENNTF_DOMINO
	 * @param parameters
	 *            their values and how to set them will depend exclusively on how the module is implemented. Check the
	 *            module documentation.
	 * @return a Session object
	 */
	public static Session getSession(String module, Object... parameters) {
		// This will be the session loaded depending the selected module
		org.riverframework.module.Session _session = null;

		// Trying to retrieve the session from the map
		AbstractSession session = map.get(module);

		if (session == null || !session.isOpen()) {
			// If not exists or is closed, we create it using the factory

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
	 * Calls the Session close method to free resources, memory, etc. Also, it will remove it from its
	 * internal table.
	 * 
	 * @param module
	 *            the package's full name
	 */
	public static void closeSession(String module) {
		AbstractSession session = map.get(module);
		if (session != null) {
			Method method;
			try {
				method = AbstractSession.class.getDeclaredMethod("protectedClose");
				method.setAccessible(true);
				method.invoke(session);
			} catch (Exception e) {
				throw new RiverException(e);
			}
			map.remove(module);
		}
	}
}
