package org.riverframework.wrapper.lotus.domino;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Session;

public class DefaultSession extends DefaultBase implements Session<org.openntf.domino.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private final DefaultFactory factory = DefaultFactory.getInstance();

	private volatile org.openntf.domino.Session __session = null;
	private String objectId = null;

	protected DefaultSession(org.riverframework.wrapper.Session<org.openntf.domino.Base> dummy, org.openntf.domino.Session obj) {
		__session = obj;
		synchronized (this){
			objectId = calcObjectId(__session);
		}

		log.fine("ObjectId:" + getObjectId());
	}

	public static String calcObjectId(org.openntf.domino.Session  __session) {
		String objectId = "";

		if (__session != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(__session.getServerName());
			sb.append(River.ID_SEPARATOR);
			sb.append(__session.getUserName());
			sb.append(River.ID_SEPARATOR);
			sb.append(__session.hashCode());

			objectId = sb.toString();
		}

		return objectId;
	}

	public DefaultFactory getFactory() {
		return factory;
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	@Override
	public org.openntf.domino.Session getNativeObject() {
		return __session;
	}

	@Override
	public boolean isOpen() {
		return (__session != null);
	}

	@Override
	public Database<org.openntf.domino.Base> createDatabase (String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		Database<org.openntf.domino.Base> _database = null;
		org.openntf.domino.Database __database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		org.openntf.domino.DbDirectory dir = __session.getDbDirectory(location[0]);
		boolean found = false;
		org.openntf.domino.Database db = dir.getFirstDatabase(org.openntf.domino.DbDirectory.DATABASE);
		while (db != null) {
			String fn = db.getFileName();
			if (fn.equalsIgnoreCase(location[1])) found = true;
			db = dir.getNextDatabase(); 
		}

		synchronized (this){
			if (!found) {
				__database = dir.createDatabase(location[1]);
				_database = getFactory().getDatabase(__database);
			}
			else {
				_database = getFactory().getDatabase(null);
			}
		}

		// CHECKING dir.recycle(); 	// To recycle or not to recycle... That is the question.

		return _database;
	}

	@Override
	public Database<org.openntf.domino.Base> getDatabase(String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		synchronized (this){
			org.openntf.domino.Database __database = null;

			if (location.length != 2)
				throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

			String server = location[0];
			String path = location[1];

			if (path.length() == 16) {
				log.finer("Trying with a replica ID");
				boolean res = false;
				__database = __session.getDatabase(null, null);
				res = __database.openByReplicaID(server, path);
				if (!res) __database = null;
			}

			if (__database == null || !__database.isOpen()) {
				log.finer("Trying with a file path");
				__database = __session.getDatabase(server, path, false);
			}

			if (__database != null && !__database.isOpen()) {
				log.finer("The database could not be opened");
				try {
					// __database.recycle();
				} catch (Exception e) {
					throw new RiverException(e);
				} finally {	
					__database = null;
				}
			}

			Database<org.openntf.domino.Base> database = getFactory().getDatabase(__database);
			return database;
		}
	}

	@Override
	public String getUserName() {
		String userName = "";

		userName = __session.getUserName();

		log.finest("getUserName=" + userName);
		return userName;
	}

	@Override
	public void close() {
		log.fine("Closing factory");
		getFactory().close();

		log.fine("Recycling the session");
		__session = null;

		log.info("Session closed.");
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
