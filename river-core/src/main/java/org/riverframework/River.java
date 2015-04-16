package org.riverframework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	public static final String MODULE_LOTUS_DOMINO = "org.riverframework.module.lotus.domino";
	public static final String MODULE_ORG_OPENNTF_DOMINO = "org.riverframework.module.org.openntf.domino";
	public static final String MODULE_HAZELCAST = "org.riverframework.module.hazelcast";

	public static final Logger LOG_ROOT = Logger.getLogger("org.riverframework");
	public static final Logger LOG_CORE = Logger.getLogger("org.riverframework.core");
	public static final Logger LOG_MODULE = Logger.getLogger("org.riverframework.module");
	public static final Logger LOG_MODULE_LOTUS_DOMINO = Logger.getLogger("org.riverframework.module.lotus.domino");
	public static final Logger LOG_MODULE_ORG_OPENNTF_DOMINO = Logger.getLogger("org.riverframework.module.org.openntf.domino");
	public static final Logger LOG_MODULE_HAZELCAST = Logger.getLogger("org.riverframework.module.hazelcast");

	private final static Map<String, AbstractSession> map = new HashMap<String, AbstractSession>();

	static {
		setLevel(LOG_ROOT, Level.SEVERE);
	}

	private River() {
		// Exists only to defeat instantiation
	}

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

		if (session != null && session.isOpen()) {
			// There is an open session
			if (parameters.length > 0) {
				// and the user is trying to open a new one
				throw new RiverException("There is already an open session for the module " + module +
						". You must close the current before opening a new one.");
			}
			// If there are no parameters, we just return the current opened session
		} else {
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
				if (parameters.length > 0) {
					// There are parameters. So, we try to create a new one.
					method = clazzFactory.getDeclaredMethod("createSession", Object[].class);
					method.setAccessible(true);
					_session = (org.riverframework.module.Session) method.invoke(null, new Object[] { parameters });
				} else {
					// There are no parameters. We create a closed session.
					_session = null;
				}

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
		Session session = map.get(module);
		if (session != null) {
			Method method;
			try {
				method = session.getClass().getSuperclass().getDeclaredMethod("protectedClose");
				method.setAccessible(true);
				method.invoke(session);
			} catch (InvocationTargetException e) {
				throw new RiverException(e.getCause().getMessage(), e);
			} catch (Exception e) {
				throw new RiverException(e);
			}
			map.remove(module);
		}
	}

	public static void setLevel(Logger log, Level level) {
		for (Handler h : log.getHandlers()) {
			log.removeHandler(h);
		}

		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(level);
		log.addHandler(handler);
		log.setLevel(level);

	}

}
