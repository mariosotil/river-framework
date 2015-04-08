package org.riverframework.module.lotus.domino;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import lotus.domino.NotesException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.RiverException;
import org.riverframework.module.Database;

public class DefaultSession implements org.riverframework.module.Session {
	private lotus.domino.Session _session = null;
	private Map<Integer, WeakReference<lotus.domino.Base>> registeredObjects = null;
	private Map<Integer, WeakReference<Vector<lotus.domino.Item>>> registeredVectors = null;
	private UUID sessionUUID = null;

	public DefaultSession(lotus.domino.Session obj) {
		_session = obj;
		registeredObjects = new HashMap<Integer, WeakReference<lotus.domino.Base>>();
		registeredVectors = new HashMap<Integer, WeakReference<Vector<lotus.domino.Item>>>();
		sessionUUID = UUID.randomUUID();
	}

	@Override
	public String getObjectId() {
		return sessionUUID.toString();
	}

	public void registerObject(lotus.domino.Base obj) {
		if (obj != null) {
			int id = obj.hashCode();
			if (registeredObjects.get(id) == null) {
				registeredObjects.put(id, new WeakReference<lotus.domino.Base>(obj));
			}
		}
	}

	public void registerVector(Vector<lotus.domino.Item> obj) {
		if (obj != null) {
			int id = obj.hashCode();
			if (registeredVectors.get(id) == null) {
				registeredVectors.put(id, new WeakReference<Vector<lotus.domino.Item>>(obj));
			}
		}
	}

	@Override
	public Object getReferencedObject() {
		return _session;
	}

	@Override
	public boolean isOpen() {
		return (_session != null);
	}

	@Override
	public Database getDatabase(String... location) {
		lotus.domino.Database _database = null;

		if (location.length != 2)
			throw new RiverException("It is expected two parameters: server and path, or server and replicaID");

		String server = location[0];
		String path = location[1];

		try {
			if (path.length() == 16) {
				boolean res = false;
				_database = _session.getDatabase(null, null);
				res = _database.openByReplicaID(server, path);
				if (!res)
					_database = null;
			}

			if (_database == null || !_database.isOpen()) {
				_database = _session.getDatabase(server, path, false);
			}

			if (_database != null && !_database.isOpen()) {
				_database = null;
			}
		} catch (NotesException e) {
			try {
				if (_database != null)
					_database.recycle();
			} catch (NotesException e1) {
			} finally {
				_database = null;
			}

			throw new RiverException(e);
		}

		Database database = new DefaultDatabase(this, _database);
		return database;
	}

	@Override
	public String getUserName() {
		try {
			return _session.getUserName();
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}

	@Override
	public void close() {
		try {
			// Recycling all registered objects before closing the session
			Iterator<Map.Entry<Integer, WeakReference<lotus.domino.Base>>> itObj = registeredObjects.entrySet().iterator();
			while (itObj.hasNext()) {
				Map.Entry<Integer, WeakReference<lotus.domino.Base>> entry = itObj.next();
				WeakReference<lotus.domino.Base> ref = entry.getValue();
				if (ref != null) {
					lotus.domino.Base obj = ref.get();
					if (obj != null) {
						obj.recycle();
						obj = null;
					}
					ref = null;
				}
			}

			// Recycling all the items vectors before closing the session
			Iterator<Map.Entry<Integer, WeakReference<Vector<lotus.domino.Item>>>> itVec = registeredVectors.entrySet().iterator();
			while (itVec.hasNext()) {
				Map.Entry<Integer, WeakReference<Vector<lotus.domino.Item>>> entry = itVec.next();
				WeakReference<Vector<lotus.domino.Item>> ref = entry.getValue();
				if (ref != null) {
					Vector<lotus.domino.Item> vec = ref.get();
					Iterator<lotus.domino.Item> it2 = vec.iterator();
					while (it2.hasNext()) {
						lotus.domino.Item obj = it2.next();
						if (obj != null) {
							obj.recycle();
							obj = null;
						}
					}
					it2 = null;
					vec = null;
					ref = null;
				}
			}

			if (_session != null)
				_session.recycle();
		} catch (NotesException e) {
			throw new RiverException(e);
		} finally {
			_session = null;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
