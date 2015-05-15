package org.riverframework.wrapper.lotus.domino;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;

public class DefaultSession implements org.riverframework.wrapper.Session {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	private lotus.domino.Session __session = null;
	private UUID sessionUUID = null;

	public DefaultSession(lotus.domino.Session obj) {
		__session = obj;
		sessionUUID = UUID.randomUUID();

		log.fine("ObjectId:" + getObjectId());
	}

	@Override
	public String getObjectId() {
		return sessionUUID.toString();
	}

//	static synchronized void getObject(Base wrapper) {
//		if (wrapper != null) {
//			String id = wrapper.getObjectId();
//			DominoReference ref = pool.get(id);
//			if (ref == null) {
//				if (log.isLoggable(Level.FINEST)) {
//					lotus.domino.Base nativeObject = (lotus.domino.Base) wrapper.getNativeObject();
//					log.finest("Registering object: id=" + id +  "wrapper=" + wrapper.getClass().getName() + " (" + wrapper.hashCode() 
//							+ ") native=" + nativeObject.getClass().getName() + " (" + nativeObject.hashCode() + ")");
//				}
//
//				pool.put(id, new DominoReference(wrapper));
//			} else {
//				Base old = wrapper;
//				wrapper = ref.getWrapperObject();
//
//				if (log.isLoggable(Level.FINEST)) {
//					log.finest("Changing wrapper for the registered one: id=" + id + " wrapper=" + wrapper.getClass().getName() + " (" + wrapper.hashCode() + 
//							")  Destroying old=" + old.getClass().getName() + " (" + old.hashCode() + ")");
//				}
//
//				old.close();
//			}
//		}
//	}

	/**
	 * Recycle a Lotus Notes object. If an exception is raised, the method will log it as a WARNING and will let
	 * continue the execution. 
	 * @param obj the object to be recycled
	 */
//	static synchronized void recycleObject(String key) {
//		DominoReference ref = pool.get(key);
//		
//		if (ref != null && ref.getWrapperObject() == null) {
//			lotus.domino.Base obj = ref.getNativeObject();
//			try {
//				obj.recycle();
//			} catch (NotesException e) {
//				log.warning("There was a problem recycling the object with id=" + key);
//			}
//			obj = null;
//
//			pool.remove(key);
//		}
//	}

	@Override
	public lotus.domino.Session getNativeObject() {
		return __session;
	}

	@Override
	public boolean isOpen() {
		return (__session != null);
	}

	@Override
	public Database getDatabase(String... location) {
		log.fine("location=" + Arrays.deepToString(location));

		lotus.domino.Database _database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		try {
			if (path.length() == 16) {
				log.finer("Trying with a replica ID");
				boolean res = false;
				_database = __session.getDatabase(null, null);
				res = _database.openByReplicaID(server, path);
				if (!res) _database = null;
			}
		} catch (NotesException e) {
			try {
				if (_database != null) _database.recycle();
			} catch (NotesException e1) {
				// Do nothing
			} finally {
				_database = null;
			}
		}

		try {
			if (_database == null || !_database.isOpen()) {
				log.finer("Trying with a file path");
				_database = __session.getDatabase(server, path, false);
			}
		} catch (NotesException e) {
			try {
				if (_database != null) _database.recycle();
			} catch (NotesException e1) {
				// Do nothing
			} finally {
				_database = null;
			}
		}

		try {
			if (_database != null && !_database.isOpen()) {
				log.finer("The database could not be opened");
				_database.recycle();
				_database = null;
			}
		} catch (NotesException e) {
			throw new RiverException(e);
		}

		Database database = Factory.createDatabase(this, _database);
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

	protected void recycling() {
		Factory.close();
	}

	@Override
	public void close() {
		log.fine("Closing session");

		recycling();

		log.fine("Closing wrapper objects");


		log.fine("Recycling the session");
		try {
			__session.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
