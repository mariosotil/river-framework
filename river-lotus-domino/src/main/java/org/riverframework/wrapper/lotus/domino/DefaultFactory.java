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

import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class DefaultFactory implements org.riverframework.wrapper.Factory {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected volatile ConcurrentHashMap<String, WeakReference<Base>> cache = null;
	protected volatile LinkedList<Reference<? extends Base>> list = null;
	protected volatile ReferenceQueue<Base> queue = null;
	private static DefaultFactory instance = null;
	protected org.riverframework.wrapper.Session _session = null;
	private NativeReferenceCollector rc = null;

	protected DefaultFactory(org.riverframework.wrapper.Session _session) {
		this._session  = _session;

		cache = new ConcurrentHashMap<String, WeakReference<Base>>();
		list = new LinkedList<Reference<? extends Base>>();
		queue = new ReferenceQueue<Base>();
		rc = new NativeReferenceCollector(queue, list);
		rc.start();
	}

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

	private void addToCache(String id, Base _wrapper) {
		if (log.isLoggable(Level.FINEST)) {
			Object __obj = _wrapper.getNativeObject();

			StringBuilder sb = new StringBuilder();
			sb.append("Registering: id=");
			sb.append(id);
			sb.append(" wrapper=");
			sb.append(_wrapper.getClass().getName());
			sb.append(" (");
			sb.append(_wrapper.hashCode());
			sb.append(") native=");
			sb.append(__obj.getClass().getName());
			sb.append(" (");
			sb.append(__obj.hashCode());
			sb.append(")");

			log.finest(sb.toString());
		}

		cache.put(id, new WeakReference<Base>(_wrapper));
		list.add(new NativeReference(_wrapper, queue));
	}

	protected Base retrieveFromCache(String id) {
		Base _wrapper = null;
		WeakReference<Base> ref = cache.get(id);

		if (ref != null) {
			//It's already registered
			_wrapper = ref.get();

			if (_wrapper == null) {
				// Removing the id from the cache
				cache.remove(id);
			} else if(_wrapper.getNativeObject() == null) {
				// Removing the id from the cache, and freeing the weak reference
				cache.remove(id);
				ref.clear();
				_wrapper = null;
			} else {
				if (log.isLoggable(Level.FINEST)) {
					Object __obj = _wrapper.getNativeObject();

					StringBuilder sb = new StringBuilder();
					sb.append("Retrieved from cache: id=");
					sb.append(id);
					sb.append(" wrapper=");
					sb.append(_wrapper.getClass().getName());
					sb.append(" (");
					sb.append(_wrapper.hashCode());
					sb.append(") native=");
					sb.append(__obj.getClass().getName());
					sb.append(" (");
					sb.append(__obj.hashCode());
					sb.append(")");

					log.finest(sb.toString());
				}
			}
		}

		return _wrapper;
	}

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

	@SuppressWarnings("unchecked")
	public <U extends Base> U getWrapper(Class<U> outputClass, Class<? extends lotus.domino.Base> inputClass, Object __obj) {
		U _wrapper = null;

		try {
			Method methodCalcObjectId = outputClass.getDeclaredMethod("calcObjectId", inputClass);

			if (__obj != null) {				
				if (!(inputClass.isAssignableFrom(__obj.getClass()))) 
					throw new RiverException("Expected an object " + inputClass.getName());
				
				String id = (String) methodCalcObjectId.invoke(null, __obj);
				_wrapper = (U) retrieveFromCache(id);
			}

			if (_wrapper == null) {
				Constructor<?> constructor = outputClass
						.getDeclaredConstructor(org.riverframework.wrapper.Session.class, inputClass);
				constructor.setAccessible(true);
				_wrapper = outputClass.cast(constructor.newInstance(_session, __obj));
			}

			if (__obj != null) {				
				String id = (String) methodCalcObjectId.invoke(null, __obj);
				addToCache(id, _wrapper);
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return _wrapper;		
	}

	@Override
	public Database getDatabase(Object __obj) {
		return getWrapper(DefaultDatabase.class, lotus.domino.Database.class, __obj);
	}

	@Override
	public Document getDocument(Object __obj) {
		return getWrapper(DefaultDocument.class, lotus.domino.Document.class, __obj);
	}

	@Override
	public View getView(Object __obj) {
		return getWrapper(DefaultView.class, lotus.domino.View.class, __obj);
	}

	@Override
	public DocumentIterator getDocumentIterator(Object __obj) {
		DocumentIterator _iterator = null;

		if(__obj instanceof lotus.domino.DocumentCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.DocumentCollection.class, __obj);

		} else if(__obj instanceof lotus.domino.ViewEntryCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.ViewEntryCollection.class, __obj);

		} else if(__obj instanceof lotus.domino.View) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.View.class, __obj);

		} else {
			throw new RiverException("Expected an object lotus.domino.View, DocumentCollection, or ViewEntryCollection.");
		}

		return _iterator;
	}

	void close() {
		Set<String> keys = cache.keySet();
		for(String key : keys) {
			WeakReference<Base> ref = cache.get(key);
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

		rc.terminate();
		log.fine("Cleaning the last objects in the phantom reference list: " + list.size());
		list.clear();
		log.fine("Cleaning the last objects in the cache: " + cache.size());
		cache.clear();
		log.info("Factory closed.");
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}