package org.riverframework.wrapper.org.openntf.domino;

import java.util.Arrays;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.Session;

public class DefaultSession extends AbstractBaseOrgOpenntfDomino<org.openntf.domino.Session> implements Session<org.openntf.domino.Session> {
	private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	protected DefaultSession(org.riverframework.wrapper.Session<org.openntf.domino.Session> dummy, org.openntf.domino.Session __native) {
		super(dummy, __native);
		_factory = DefaultFactory.getInstance();
	}

	@Override
	public String calcObjectId(org.openntf.domino.Session  __session) {
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

	public Factory<org.openntf.domino.Base<?>> getFactory() {
		return _factory;
	}

	@Override
	public boolean isOpen() {
		return (__native != null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Database<org.openntf.domino.Database> createDatabase (String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		Database<org.openntf.domino.Database> _database = null;
		org.openntf.domino.Database __database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		org.openntf.domino.DbDirectory dir = __native.getDbDirectory(location[0]);
		boolean found = false;
		org.openntf.domino.Database db = dir.getFirstDatabase(org.openntf.domino.DbDirectory.DATABASE);
		while (db != null) {
			String fn = db.getFileName();
			if (fn.equalsIgnoreCase(location[1])) found = true;
			db = dir.getNextDatabase(); 
		}

		if (!found) {
			__database = dir.createDatabase(location[1]);
			_database = (Database<org.openntf.domino.Database>) getFactory().getDatabase(__database);
		}
		else {
			_database = (Database<org.openntf.domino.Database>) getFactory().getDatabase(null);
		}

		return _database;
	}

	@Override
	public Database<org.openntf.domino.Database> getDatabase(String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		org.openntf.domino.Database __database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		if (path.length() == 16) {
			log.finer("Trying with a replica ID");
			boolean res = false;
			__database = __native.getDatabase(null, null);
			res = __database.openByReplicaID(server, path);
			if (!res) __database = null;
		}

		if (__database == null || !__database.isOpen()) {
			log.finer("Trying with a file path");
			__database = __native.getDatabase(server, path, false);
		}

		if (__database != null && !__database.isOpen()) {
			log.finer("The database could not be opened");
			__database = null;
		}

		@SuppressWarnings("unchecked")
		Database<org.openntf.domino.Database> database = (Database<org.openntf.domino.Database>) getFactory().getDatabase(__database);
		return database;
	}

	@Override
	public String getUserName() {
		String userName = "";

		userName = __native.getUserName();

		log.finest("getUserName=" + userName);
		return userName;
	}

	@Override
	public void close() {
		log.fine("Closing factory");
		getFactory().close();

		__native = null;

		log.info("Session closed.");
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + objectId + ")";
	}
}
