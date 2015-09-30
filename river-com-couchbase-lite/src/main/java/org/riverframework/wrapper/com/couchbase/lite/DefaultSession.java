package org.riverframework.wrapper.com.couchbase.lite;

import java.util.Arrays;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.Session;

import com.couchbase.lite.CouchbaseLiteException;

public class DefaultSession extends AbstractBaseCouchbaseLite<com.couchbase.lite.Manager> implements Session<com.couchbase.lite.Manager> {
	private static final Logger log = River.LOG_WRAPPER_COM_COUCHBASE_LITE;

	protected DefaultSession(org.riverframework.wrapper.Session<com.couchbase.lite.Manager> dummy, com.couchbase.lite.Manager __native) {
		super(dummy, __native);
		_factory = DefaultFactory.getInstance();
	}

	@Override
	public String calcObjectId(com.couchbase.lite.Manager  __session) {
		String objectId = "";

		if (__session != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("LOCAL");
			sb.append(River.ID_SEPARATOR);
			sb.append("ANONYMOUS");
			sb.append(River.ID_SEPARATOR);
			sb.append(__session.hashCode());

			objectId = sb.toString();
		}

		return objectId;
	}

	public Factory<Object> getFactory() {
		return _factory;
	}

	@Override
	public boolean isOpen() {
		return (__native != null);
	}

	@Override
	public Database<com.couchbase.lite.Database> createDatabase (String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		com.couchbase.lite.Database __database = null;
		try {
			__database = __native.getDatabase(location[0].toLowerCase());
		} catch (CouchbaseLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Database<com.couchbase.lite.Database> _database = (Database<com.couchbase.lite.Database>) getFactory().getDatabase(__database);

		return _database;
	}

	@Override
	public Database<com.couchbase.lite.Database> getDatabase(String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		com.couchbase.lite.Database __database = null;

		@SuppressWarnings("unchecked")
		Database<com.couchbase.lite.Database> database = (Database<com.couchbase.lite.Database>) getFactory().getDatabase(__database);
		return database;
	}

	@Override
	public String getUserName() {
		String userName = "ANONYMOUS";
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
