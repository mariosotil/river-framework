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

public class DefaultSession extends AbstractBaseLotusDomino<lotus.domino.Session> implements Session<lotus.domino.Session> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected DefaultSession(org.riverframework.wrapper.Session<lotus.domino.Session> dummy, lotus.domino.Session __native) {
		super(dummy, __native);
		_factory = DefaultFactory.getInstance();
	}

	@Override
	public boolean isRecycled() {
		if (_factory.getIsRemoteSession()) {
			// There's no a deleted field for the Session class
			return false;
		} else {
			return isObjectRecycled(__native);
		}
	}

	@Override
	public String calcObjectId(lotus.domino.Session  __session) {
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
	public boolean isOpen() {
		return (__native != null && !isRecycled()); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Database<lotus.domino.Database> createDatabase (String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		Database<lotus.domino.Database> _database = null;
		lotus.domino.Database __database = null;

		_database = getDatabase(location);

		if (!_database.isOpen()) {
			try {
				lotus.domino.DbDirectory dir = __native.getDbDirectory(location[0]);
				__database = dir.createDatabase(location[1]);
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			_database = (Database<lotus.domino.Database>) getFactory().getDatabase(__database);

		} else {
			_database = (Database<lotus.domino.Database>) getFactory().getDatabase(null);
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
				__database = __native.getDatabase(null, null);
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
				__database = __native.getDatabase(server, path, false);
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
			userName = __native.getUserName();
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
			__native.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			__native = null;
		}

		log.info("Session closed.");
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
