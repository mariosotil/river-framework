package org.riverframework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.core.DefaultSession;
import org.riverframework.core.Session;
import org.riverframework.utils.LoggerHelper;

/**
 * This static class is used for load a River NoSQL wrapper, and create and close core Session objects for control the
 * data.
 * 
 * The session objects are used for create the Database, Document, View and DocumentList objects.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public class River {
	public static final String LOTUS_DOMINO = "org.riverframework.wrapper.lotus.domino";
	public static final String ORG_OPENNTF_DOMINO = "org.riverframework.wrapper.org.openntf.domino";
	public static final String HAZELCAST = "org.riverframework.wrapper.hazelcast";

	public static final Logger LOG_ROOT = Logger.getLogger("org.riverframework");
	public static final Logger LOG_CORE = Logger.getLogger("org.riverframework.core");
	public static final Logger LOG_WRAPPER = Logger.getLogger("org.riverframework.wrapper");
	public static final Logger LOG_WRAPPER_LOTUS_DOMINO = Logger.getLogger("org.riverframework.wrapper.lotus.domino");
	public static final Logger LOG_WRAPPER_ORG_OPENNTF_DOMINO = Logger.getLogger("org.riverframework.wrapper.org.openntf.domino");
	public static final Logger LOG_WRAPPER_HAZELCAST = Logger.getLogger("org.riverframework.wrapper.hazelcast");

	public static final String ID_SEPARATOR = "**"; 
	
	public static Object lock = new Object();
	private final static Map<String, Session> map = new HashMap<String, Session>();

	static {
		new LoggerHelper(LOG_ROOT).clearHandlers().setLevel(Level.SEVERE);
	}

	private River() {
		// Exists only to defeat instantiation
	}

	/**
	 * Loads a wrapper that wraps libraries as lotus.domino or org.openntf.domino and creates a core Session
	 * object. Its behavior will depend on how the wrapper is implemented. Anyway, this method creates the session just
	 * one time and returns the same every time is called. To free the memory, resources, etc., it's necessary to call
	 * the close method at the end of the program or process.
	 * 
	 * @param wrapper
	 *            the package's full name. You can use the constants defined as River.wrapper_LOTUS_DOMINO or
	 *            River.wrapper_ORG_OPENNTF_DOMINO
	 * @param parameters
	 *            their values and how to set them will depend exclusively on how the wrapper is implemented. Check the
	 *            wrapper documentation.
	 * @return a Session object
	 */
	public static Session getSession(String wrapper, Object... parameters) {
		// This will be the session loaded depending the selected wrapper
		org.riverframework.wrapper.Session<?> _session = null;

		// Trying to retrieve the session from the map
		Session session = map.get(wrapper);

		if (session != null && session.isOpen()) {
			// There is an open session
			if (parameters.length > 0) {
				// and the user is trying to open a new one
				throw new RiverException("There is already an open session for the wrapper " + wrapper +
						". You must close the current before opening a new one.");
			}
			// If there are no parameters, we just return the current opened session
		} else {
			// If not exists or is closed, we create it using the factory

			Class<?> clazzFactory = null;

			try {
				clazzFactory = Class.forName(wrapper + ".DefaultFactory");
			} catch (ClassNotFoundException e) {
				throw new RiverException("The wrapper '" + wrapper + "' can not be loaded. If you are using an non-official wrapper, " +
						"check the wrapper name and its design. Check the CLASSPATH.");
			}
			
			try {
				Method method = clazzFactory.getDeclaredMethod("getInstance");
				method.setAccessible(true);
				org.riverframework.wrapper.Factory<?> _factory = (org.riverframework.wrapper.Factory<?>) method.invoke(null);

				if (_factory == null)
					throw new RiverException("The factory could not be loaded.");

				if (parameters.length > 0) {
					// There are parameters. So, we try to create a new one.
					_session = (org.riverframework.wrapper.Session<?>) _factory.getSession(parameters);
				} else {
					// There are no parameters. We create a closed session.
					_session = null;
				}
				
				Constructor<?> constructor = DefaultSession.class.getDeclaredConstructor(org.riverframework.wrapper.Session.class);
				constructor.setAccessible(true);
				session = (DefaultSession) constructor.newInstance(_session);
				
			} catch (Exception e) {
				throw new RiverException("There's a problem opening the session. Maybe, you will need to check the parameters.", e);
			}

			map.put(wrapper, session);
		}

		return session;
	}

	/**
	 * Calls the Session close method to free resources, memory, etc. Also, it will remove it from its
	 * internal table.
	 * 
	 * @param wrapper
	 *            the package's full name
	 */
	public static void closeSession(String wrapper) {
		Session session = map.get(wrapper);
		if (session != null) {
			Method method;
			try {
				method = session.getClass().getDeclaredMethod("protectedClose");
				method.setAccessible(true);
				method.invoke(session);
			} catch (InvocationTargetException e) {
				throw new RiverException(e.getCause().getMessage(), e);
			} catch (Exception e) {
				throw new RiverException(e);
			}
			map.remove(wrapper);
		}
	}
}
