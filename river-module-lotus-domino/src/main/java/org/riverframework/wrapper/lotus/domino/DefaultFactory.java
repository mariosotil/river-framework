package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

public class DefaultFactory implements org.riverframework.wrapper.Factory {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected ConcurrentHashMap<String, WeakReference<Base>> map = null;
	protected LinkedList<Reference> list = null;
	//protected ConcurrentHashMap<String, Reference> list = null;
	protected ReferenceQueue<Base> queue = null;
	private static DefaultFactory instance = null;
	protected org.riverframework.wrapper.Session _session = null;

	protected DefaultFactory(org.riverframework.wrapper.Session _session) {
		this._session  = _session;

		map = new ConcurrentHashMap<String, WeakReference<Base>>();
		list = new LinkedList<Reference>();
		//list = new ConcurrentHashMap<String, Reference>();
		queue = new ReferenceQueue<Base>();

		Thread referenceThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
						System.gc();
						Thread.sleep(100);
						Reference ref = null;
						do {
							ref = (Reference) queue.remove(100);
							if (ref != null) {
								ref.close();
							}
						} while (ref != null);
						//list.remove(ref.getObjectId()); // <== ò_ó!!!							
					} catch (Exception ex) {
						log.warning("Exception at recycling");
					}
				}
			}
		};
		referenceThread.setName("org.riverframework.wrapper.lotus.domino.Pool.referenceThread");
		referenceThread.setDaemon(true);
		referenceThread.start();

		//		Thread gcThread = new Thread() {
		//			@Override
		//			public void run() {
		//				while (true) {
		//					try {
		//						Thread.sleep(1000);
		//					} catch (InterruptedException e) {
		//						// Do nothing
		//					}
		//					System.gc();
		//				}
		//			}
		//		};
		//		gcThread.setName("org.riverframework.wrapper.lotus.domino.Pool.gcThread");
		//		gcThread.setDaemon(true);
		//		gcThread.start();

	}

	// Lazy Initialization (If required then only)
	public static DefaultFactory getInstance(org.riverframework.wrapper.Session _session) {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (DefaultFactory.class) {
				if (instance == null) {
					instance = new DefaultFactory(_session);
				}
			}
		}
		return instance;
	}

	protected void checkObj(Base wrapper) {
		if (wrapper != null && wrapper.isOpen()) {
			String id = wrapper.getObjectId();
			WeakReference<Base> ref = map.get(id);

			if (ref == null) {
				// It's a new object
				add(id, wrapper);
			} else {
				//It's already registered
				Base registered = ref.get();

				if (registered == null) {
					// Replace the null value for the new wrapper
					add(id, wrapper);
				} else {
					// Preserve the pooled wrapper and replace the 'wrapper' parameter
					Base toOblivion = wrapper;
					wrapper = registered;

					if (log.isLoggable(Level.FINEST)) {
						log.finest("Changing wrapper for the registered one: id=" + id + " wrapper=" + wrapper.getClass().getName() + " ("
								+ wrapper.hashCode() + ")  Destroying old=" + toOblivion.getClass().getName() + " (" + toOblivion.hashCode() + ")");
					}

					toOblivion = null;
				}
			}
		}
	}

	private void add(String id, Base wrapper) {
		if (log.isLoggable(Level.FINEST)) {
			Object nativeObject = wrapper.getNativeObject();
			log.finest("Registering: id=" + id + " wrapper=" + wrapper.getClass().getName() + " (" + wrapper.hashCode()
					+ ") native=" + nativeObject.getClass().getName() + " (" + nativeObject.hashCode() + ")");
		}

		map.put(id, new WeakReference<Base>(wrapper));
		//list.put(id, new Reference(wrapper, queue));
		list.add(new Reference(wrapper, queue));
	}

	//	<U extends org.riverframework.wrapper.Base> U create(Class<U> clazz, lotus.domino.Base __obj) {
	//		U _obj = null;
	//
	//		try {
	//			if (__obj != null) {
	//				Class<? extends lotus.domino.Base> classObj = __obj.getClass();			
	//				Constructor<?> constructor = clazz.getDeclaredConstructor(org.riverframework.wrapper.Session.class, classObj);
	//				constructor.setAccessible(true);  //protected DefaultDatabase(org.riverframework.wrapper.Session s, lotus.domino.Database obj) {
	//				_obj = clazz.cast(constructor.newInstance(_session, __obj));
	//				checkObj(_obj);
	//			}
	//		} catch (Exception e) {
	//			throw new RiverException(e);
	//		}
	//
	//		return _obj;
	//	}	

	@SuppressWarnings("unused")
	private static Session getSession(Object... parameters) {
		if (parameters.length == 1 && parameters[0] instanceof lotus.domino.Session) {
			log.finer("Creating a session with one lotus.domino.Session parameter");
			return new DefaultSession((lotus.domino.Session) parameters[0]);
		}

		if (parameters.length == 3 && parameters[2] instanceof String) {
			log.finer("Creating a session with three parameters: server, username, password");
			try {
				return new DefaultSession(
						NotesFactory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]));
			} catch (NotesException e) {
				throw new RiverException(e);
			}
		}

		throw new RiverException(
				"Valid parameters: (A) one lotus.domino.Session or (B) three Strings in this order: server, username and password.");
	}

	@Override
	public Database getDatabase(Object __obj) {
		if (!(__obj instanceof lotus.domino.Database)) throw new RiverException("Expected an object lotus.domino.Database");

		Database _database = new DefaultDatabase(_session, (lotus.domino.Database)__obj);
		checkObj(_database);
		return _database;		
	}

	@Override
	public Document getDocument(Object __obj) {
		if (__obj != null && !(__obj instanceof lotus.domino.Document)) throw new RiverException("Expected an object lotus.domino.Document");

		Document _doc = new DefaultDocument(_session, (lotus.domino.Document) __obj);
		checkObj(_doc);
		return _doc;
	}

	@Override
	public View getView(Object __obj) {
		if (__obj != null && !(__obj instanceof lotus.domino.View)) throw new RiverException("Expected an object lotus.domino.View");

		View _view = new DefaultView(_session, (lotus.domino.View) __obj);
		checkObj(_view);
		return _view;
	}

	@Override
	public DocumentIterator getDocumentIterator(Object __obj) {
		DocumentIterator _it = null;

		if (__obj == null || __obj instanceof lotus.domino.DocumentCollection) { 
			_it = new DefaultDocumentIterator(_session, (lotus.domino.DocumentCollection) __obj);
		} else if (__obj == null || __obj instanceof lotus.domino.ViewEntryCollection) { 
			_it = new DefaultDocumentIterator(_session, (lotus.domino.ViewEntryCollection) __obj);
		} else if (__obj == null || __obj instanceof lotus.domino.View) { 
			_it = new DefaultDocumentIterator(_session, (lotus.domino.View) __obj);
		} 

		if (_it == null) throw new RiverException("Expected an object lotus.domino.DocumentCollection, lotus.domino.ViewEntryCollection or lotus.domino.View");

		checkObj(_it);
		return _it;
	}

	void close() {
		Set<String> keys = map.keySet();
		for(String key : keys) {
			WeakReference<Base> ref = map.get(key);
			if (ref != null) {
				Base wrapper = ref.get();

				if (wrapper != null) {
					lotus.domino.Base nativeObj = (lotus.domino.Base) wrapper.getNativeObject();

					if (nativeObj != null) {
						int hc = nativeObj.hashCode();
						String id = wrapper.getObjectId();
						try {							
							nativeObj.recycle();
							log.finest("Recycled: id=" + id + " (" + hc + ")");
						} catch (NotesException e) {
							log.warning("Exception after recycling object " + id + " HC=" + hc);
						}

						nativeObj = null;
					}

					wrapper = null;
				} 
			}
		}
	}
}