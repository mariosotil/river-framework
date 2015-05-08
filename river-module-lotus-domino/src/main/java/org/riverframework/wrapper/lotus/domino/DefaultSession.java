package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.utils.StringDump;
import org.riverframework.wrapper.Database;

public class DefaultSession implements org.riverframework.wrapper.Session {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	private lotus.domino.Session __session = null;
	private Map<Integer, WeakReference<lotus.domino.Base>> registeredObjects = null;
	private Map<Integer, WeakReference<Vector<lotus.domino.Item>>> registeredVectors = null;
	private UUID sessionUUID = null;

	public DefaultSession(lotus.domino.Session obj) {
		__session = obj;
		registeredObjects = new HashMap<Integer, WeakReference<lotus.domino.Base>>();
		registeredVectors = new HashMap<Integer, WeakReference<Vector<lotus.domino.Item>>>();
		sessionUUID = UUID.randomUUID();

		log.fine("ObjectId:" + getObjectId());
	}

	@Override
	public String getObjectId() {
		return sessionUUID.toString();
	}

	public void registerObject(lotus.domino.Base obj) {
		if (obj != null) {
			int id = System.identityHashCode(obj);
			if (registeredObjects.get(id) == null) {
				if (log.isLoggable(Level.FINEST)) {
					String objClass = obj.getClass().getName();
					String objContent = StringDump.dump(obj);
					log.finest("Registering object: " + objClass + " - " + objContent);
				}

				registeredObjects.put(id, new WeakReference<lotus.domino.Base>(obj));
			}
		}
	}

	public void registerVector(Vector<lotus.domino.Item> obj) {
		if (obj != null) {
			int id = System.identityHashCode(obj);
			if (registeredVectors.get(id) == null) {
				if (log.isLoggable(Level.FINEST)) {
					String objClass = obj.getClass().getName();
					String objContent = StringDump.dump(obj);
					log.finest("Registering object: " + objClass + " - " + objContent);
				}

				registeredVectors.put(id, new WeakReference<Vector<lotus.domino.Item>>(obj));
			}
		}
	}

	/**
	 * Recycle a Lotus Notes object. If an exception is raised, the method will log it as a WARNING and will let
	 * continue the execution. 
	 * @param obj the object to be recycled
	 */
	private void recycleObject(lotus.domino.Base obj) {
		if (obj != null) {			
			String objClass = obj.getClass().getName();
			String objContent = StringDump.dump(obj);
			log.finest("Recycling object: " + objClass + " - " + objContent);

			try{
				obj.recycle();
			} catch (NotesException e) {
				log.log(Level.WARNING, "It was not possible to recycle this object: " + objClass + " - " + objContent, e);
			} 
			obj = null;
		}
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
				if (!res)
					_database = null;
			}

			if (_database == null || !_database.isOpen()) {
				log.finer("Trying with a file path");
				_database = __session.getDatabase(server, path, false);
			}

			if (_database != null && !_database.isOpen()) {
				log.finer("The database could not be opened");
				_database = null;
			}
		} catch (NotesException e) {
			recycleObject(_database);

			throw new RiverException(e);
		}

		Database database = new DefaultDatabase(this, _database);
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
		log.fine("Recycling all the items vectors before closing the session");
		Iterator<Map.Entry<Integer, WeakReference<Vector<lotus.domino.Item>>>> itVec = registeredVectors.entrySet().iterator();
		while (itVec.hasNext()) {
			Map.Entry<Integer, WeakReference<Vector<lotus.domino.Item>>> entry = itVec.next();
			WeakReference<Vector<lotus.domino.Item>> ref = entry.getValue();
			if (ref != null) {
				Vector<lotus.domino.Item> vec = ref.get();
				if (vec != null) { 
					for(int i = 0; i < vec.size(); i++) {
						lotus.domino.Item obj = vec.get(i);
						recycleObject(obj);
					}
					vec = null;
				}
				ref = null;
			}
		}

		log.fine("Recycling all registered objects before closing the session");
		Iterator<Map.Entry<Integer, WeakReference<lotus.domino.Base>>> itObj = registeredObjects.entrySet().iterator();
		while (itObj.hasNext()) {
			Map.Entry<Integer, WeakReference<lotus.domino.Base>> entry = itObj.next();
			WeakReference<lotus.domino.Base> ref = entry.getValue();
			if (ref != null) {
				lotus.domino.Base obj = ref.get();
				recycleObject(obj);
				ref = null;
			}
		}		
	}
	
	@Override
	public void close() {
		log.fine("Closing session");

		recycling();
		
		log.fine("Recycling the session");
		recycleObject(__session);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
