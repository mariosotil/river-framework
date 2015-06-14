package org.riverframework.wrapper.lotus.domino;

import java.util.Arrays;
import java.util.logging.Logger;

import lotus.domino.NotesException;


import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Session;

public class DefaultSession implements Session<lotus.domino.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private final DefaultFactory factory = DefaultFactory.getInstance();

	private volatile lotus.domino.Session __session = null;
	private String objectId = null;

	protected DefaultSession(org.riverframework.wrapper.Session<lotus.domino.Base> dummy, lotus.domino.Session obj) {
		__session = obj;
		synchronized (this){
			objectId = calcObjectId(__session);
		}

		log.fine("ObjectId:" + getObjectId());
	}

	public static String calcObjectId(lotus.domino.Session  __session) {
		String objectId = "";

		if (__session != null) {
			try {

				StringBuilder sb = new StringBuilder();
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

	public DefaultFactory getFactory() {
		return factory;
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
		return (__session != null);
	}

	@Override
	public Database<lotus.domino.Base> createDatabase (String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		Database<lotus.domino.Base> _database = null;
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

			synchronized (this){
				if (!found) {
					__database = dir.createDatabase(location[1]);
					_database = getFactory().getDatabase(__database);
				}
				else {
					_database = getFactory().getDatabase(null);
				}
			}

			// CHECKING dir.recycle();			
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		return _database;
	}

	@Override
	public Database<lotus.domino.Base> getDatabase(String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		synchronized (this){
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
					// CHECKING if (__database != null) __database.recycle();
					// CHECKING } catch (NotesException e1) {
					// Do nothing
				} finally {
					__database = null;
				}
			}

			try {
				if (__database == null || !__database.isOpen()) {
					log.finer("Trying with a file path");
					__database = __session.getDatabase(server, path, false);
				}
			} catch (NotesException e) {
				try {
					// CHECKING 	if (__database != null) __database.recycle();
					// CHECKING } catch (NotesException e1) {
					// Do nothing
				} finally {
					__database = null;
				}
			}

			try {
				if (__database != null && !__database.isOpen()) {
					log.finer("The database could not be opened");
					try {
						// CHECKING 	__database.recycle();
						// CHECKING } catch (NotesException e) {
						// CHECKING throw new RiverException(e);
					} finally {	
						__database = null;
					}
				}
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			Database<lotus.domino.Base> database = getFactory().getDatabase(__database);
			return database;
		}
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
		log.fine("Recycling the session");
		try {
			__session.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__session = null;
		}

		log.fine("Closing factory");
		getFactory().close();

		log.info("Session closed.");
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
