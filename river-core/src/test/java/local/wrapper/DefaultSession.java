package local.wrapper;

import local.mock.DatabaseException;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.Session;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultSession extends AbstractBaseNoSQL<local.mock.Session> implements Session<local.mock.Session> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected DefaultSession(Session<local.mock.Session> dummy, local.mock.Session __native) {
		super(dummy, __native);
		_factory = DefaultFactory.getInstance();
	}

	@Override
	public boolean isRecycled() {
		if (_factory.getIsRemoteSession()) {
			// There's no a deleted field for the Session class
			return false;
		} else {
			return AbstractBaseNoSQL.isObjectRecycled(__native);
		}
	}

	@Override
	public String calcObjectId(local.mock.Session  __session) {
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
			} catch (DatabaseException e) {
				throw new RiverException(e);
			}	
		}

		return objectId;
	}

	public Factory<local.mock.Base> getFactory() {
		return _factory;
	}

	@Override
	public boolean isOpen() {
		return (__native != null && !isRecycled()); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Database<local.mock.Database> createDatabase (String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		Database<local.mock.Database> _database = null;
		local.mock.Database __database = null;

		_database = getDatabase(location);

		if (!_database.isOpen()) {
			try {
				//local.mock.DbDirectory dir = __native.getDbDirectory(location[0]);
				//__database = dir.createDatabase(location[1]);
			} catch (DatabaseException e) {
				throw new RiverException(e);
			}

			_database = (Database<local.mock.Database>) getFactory().getDatabase(__database);

		} else {
			_database = (Database<local.mock.Database>) getFactory().getDatabase(null);
		}

		return _database;
	}

	@Override
	public Database<local.mock.Database> getDatabase(String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		local.mock.Database __database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		try {
			if (__database == null || !__database.isOpen()) {
				log.finer("Trying with a file path");
				__database = __native.getDatabase(server, path, false);
			}
		} catch (DatabaseException e1) {
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
		} catch (DatabaseException e) {
			throw new RiverException(e);
		}

		@SuppressWarnings("unchecked")
		Database<local.mock.Database> database = (Database<local.mock.Database>) getFactory().getDatabase(__database);
		return database;
	}

	@Override
	public String getUserName() {
		String userName = "";

		try {
			userName = __native.getUserName();
		} catch (DatabaseException e) {
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
		} catch (DatabaseException e) {
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
