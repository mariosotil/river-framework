package org.riverframework.wrapper;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.RiverException;

public abstract class AbstractFactory<T> implements org.riverframework.wrapper.Factory<T> {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected volatile ConcurrentHashMap<String, WeakReference<Base>> cache = null;
	protected volatile ReferenceQueue<Base> queue = null;
	protected org.riverframework.wrapper.Session _session = null;

	protected AbstractFactory(org.riverframework.wrapper.Session _session) {
		this._session = _session;

		cache = new ConcurrentHashMap<String, WeakReference<Base>>();
		queue = new ReferenceQueue<Base>();
	}

	protected void addToCache(String id, Base _wrapper) {
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
	}

	protected Base retrieveFromCache(String id) {
		Base _wrapper = null;
		WeakReference<Base> ref = cache.get(id);

		if (ref != null) {
			// It's already registered
			_wrapper = ref.get();

			if (_wrapper == null) {
				// Removing the id from the cache
				cache.remove(id);
			} else if (_wrapper.getNativeObject() == null) {
				// Removing the id from the cache, and freeing the weak
				// reference
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

	@SuppressWarnings("unchecked")
	public <U extends Base> U getWrapper(Class<U> outputClass, Class<? extends T> inputClass, Object __obj) {
		U _wrapper = null;

		try {
			String id = null;

			if (__obj != null) {
				if (!(inputClass.isAssignableFrom(__obj.getClass())))
					throw new RiverException("Expected an object " + inputClass.getName());

				Method methodCalcObjectId = outputClass.getDeclaredMethod("calcObjectId", inputClass);
				methodCalcObjectId.setAccessible(true);
				id = (String) methodCalcObjectId.invoke(null, __obj);
				_wrapper = (U) retrieveFromCache(id);
			}

			if (_wrapper == null) {
				Constructor<?> constructor = outputClass.getDeclaredConstructor(org.riverframework.wrapper.Session.class, inputClass);
				constructor.setAccessible(true);
				_wrapper = outputClass.cast(constructor.newInstance(_session, __obj));
			}

			if (_wrapper.getNativeObject() != null) {
				addToCache(id, _wrapper);
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return _wrapper;
	}

	@Override
	public void close() {
		log.fine("Cleaning the last objects in the cache: " + cache.size());
		cache.clear();
		log.info("Factory closed.");
	}
}