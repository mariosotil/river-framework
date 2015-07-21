package org.riverframework.wrapper.lotus.domino;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.Session;

public class DefaultSession extends AbstractBase<lotus.domino.Session> implements Session<lotus.domino.Session> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private final DefaultFactory _factory = DefaultFactory.getInstance();

	private volatile lotus.domino.Session __session = null;
	private String objectId = null;

	protected DefaultSession(org.riverframework.wrapper.Session<lotus.domino.Base> dummy, lotus.domino.Session obj) {
		__session = obj;
		objectId = calcObjectId(__session);

		log.fine("ObjectId:" + getObjectId());
	}

	@Override
	public boolean isRecycled() {
		if (_factory.getIsRemoteSession()) {
			// There's no a deleted field for the Session class
			return false;
		} else {
			return isObjectRecycled(__session);
		}
	}
	
	public static String calcObjectId(lotus.domino.Session  __session) {
		String objectId = "";

		if (__session != null) {
			try {

				StringBuilder sb = new StringBuilder(1024);
				sb.append(__session.getServerName());
				sb.append(River.ID_SEPARATOR);
				sb.append(__session.getUserName());
				sb.append(River.ID_SEPARATOR);
				sb.append(__session.hashCode());

				objectId = sb.toString();
			} catch (NotesException e) {
				throw new RiverException(e);
			}	
		}

		return objectId;
	}

	public Factory<lotus.domino.Base> getFactory() {
		return _factory;
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	@Override
	public lotus.domino.Session getNativeObject() {
		return __session;
	}

	@Override
	public boolean isOpen() {
		return (__session != null && !isRecycled()); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Database<lotus.domino.Database> createDatabase (String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		Database<lotus.domino.Database> _database = null;
		lotus.domino.Database __database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		try {
			lotus.domino.DbDirectory dir = __session.getDbDirectory(location[0]);
			boolean found = false;
			lotus.domino.Database db = dir.getFirstDatabase(lotus.domino.DbDirectory.DATABASE);
			while (db != null) {
				String fn = db.getFileName();
				if (fn.equalsIgnoreCase(location[1])) found = true;
				db = dir.getNextDatabase(); 
			}

			if (!found) {
				__database = dir.createDatabase(location[1]);
				_database = (Database<lotus.domino.Database>) getFactory().getDatabase(__database);
			}
			else {
				_database = (Database<lotus.domino.Database>) getFactory().getDatabase(null);
			}

		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return _database;
	}

	@Override
	public Database<lotus.domino.Database> getDatabase(String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		lotus.domino.Database __database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		try {
			if (path.length() == 16) {
				log.finer("Trying with a replica ID");
				boolean res = false;
				__database = __session.getDatabase(null, null);
				res = __database.openByReplicaID(server, path);
				if (!res) __database = null;
			}
		} catch (NotesException e) {
			try {
				// if (__database != null) __database.recycle();
			} catch (Exception e1) {
				log.log(Level.WARNING, "Exception while getting the database at " + server + "!!" + path, e1);
			} finally {
				__database = null;
			}
		}

		try {
			if (__database == null || !__database.isOpen()) {
				log.finer("Trying with a file path");
				__database = __session.getDatabase(server, path, false);
			}
		} catch (NotesException e1) {
			try {
				// if (__database != null) __database.recycle();
			} catch (Exception e) {
				log.log(Level.WARNING, "Exception while getting the database at " + server + "!!" + path, e1);
			} finally {
				__database = null;
			}
		}

		try {
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
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		Database<lotus.domino.Database> database = (Database<lotus.domino.Database>) getFactory().getDatabase(__database);
		return database;
	}

	@Override
	public String getUserName() {
		String userName = "";

		try {
			userName = __session.getUserName();
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		log.finest("getUserName=" + userName);
		return userName;
	}

	@Override	
	public void close() {
		log.fine("Closing factory");
		getFactory().close();

		log.fine("Recycling the session");
		try {
			__session.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__session = null;
		}

		log.info("Session closed.");
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
