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

public abstract class AbstractFactory<N> implements org.riverframework.wrapper.Factory<N> {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected volatile ConcurrentHashMap<String, WeakReference<? extends Base<N>>> softWrapperMap = null;
	protected volatile ConcurrentHashMap<String, NativeReference<N>> phantomWrapperMap = null;
	protected volatile ReferenceQueue<Base<N>> queue = null;
	protected Class<? extends NativeReference<N>> nativeReferenceClass = null;
	protected Class<? extends NativeReferenceCollector> nativeReferenceCollectorClass = null;
	protected org.riverframework.wrapper.Session _session = null;
	protected NativeReferenceCollector rc = null;

	protected AbstractFactory(org.riverframework.wrapper.Session _session, Class<? extends NativeReference<N>> nativeReferenceClass,
			Class<? extends NativeReferenceCollector> nativeReferenceCollectorClass) {
		this._session = _session;
		this.nativeReferenceClass = nativeReferenceClass;
		this.nativeReferenceCollectorClass = nativeReferenceCollectorClass;

		softWrapperMap = new ConcurrentHashMap<String, WeakReference<? extends Base<N>>>();
		phantomWrapperMap = new ConcurrentHashMap<String, NativeReference<N>>();
		queue = new ReferenceQueue<Base<N>>();

		try {
			Constructor<?> constructor = nativeReferenceCollectorClass.getDeclaredConstructor(Class.class, ConcurrentHashMap.class,
					ConcurrentHashMap.class, ReferenceQueue.class);
			constructor.setAccessible(true);
			rc = nativeReferenceCollectorClass
					.cast(constructor.newInstance(nativeReferenceClass, softWrapperMap, phantomWrapperMap, queue));
			((Thread) rc).start();
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	protected void addToCache(String id, Base<N> _wrapper) {
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

		WeakReference<Base<N>> weak = new WeakReference<Base<N>>(_wrapper);
		softWrapperMap.put(id, weak);

		try {
			Constructor<?> constructor = nativeReferenceClass.getDeclaredConstructor(Base.class, ReferenceQueue.class);
			constructor.setAccessible(true);
			NativeReference<N> ref = nativeReferenceClass.cast(constructor.newInstance(_wrapper, queue));
			phantomWrapperMap.put(id, ref);
		} catch (Exception e) {
			throw new RiverException(e);
		}

	}

	protected Base<N> retrieveFromCache(String id) {
		Base<N> _wrapper = null;

		// N __native = phantomWrapperMap.get(id).getNativeObject();
		// if (__native == null) {
		// log.finest("Native object is null. id=" + id);
		// } else {
		// log.finest("Native object is NOT null. id=" + id);
		// }

		WeakReference<? extends Base<N>> ref = softWrapperMap.get(id);

		if (ref != null) {
			// It's already registered
			log.finest("Already registered. id=" + id);
			_wrapper = ref.get();

			if (_wrapper == null) {
				// Removing the id from the cache
				log.finest("Wrapper is null. id=" + id);
				softWrapperMap.remove(id);
				phantomWrapperMap.remove(id);
			} else if (_wrapper.getNativeObject() == null) {
				// Removing the id from the cache, and freeing the weak and
				// phantom references
				log.finest("Native object is null. id=" + id);
				softWrapperMap.remove(id);
				phantomWrapperMap.remove(id);
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
		} else {
			log.finest("Not registered. id=" + id);
		}

		return _wrapper;
	}

	@Override
	public void logStatus() {
		log.finest("cache=" + softWrapperMap.size());
		rc.logStatus();
	}

	@SuppressWarnings("unchecked")
	public <U extends Base<N>> U getWrapper(Class<U> outputClass, Class<? extends N> inputClass, Object __obj) {
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

				if (_wrapper.getNativeObject() != null) {
					addToCache(id, _wrapper);
				}
			}

		} catch (Exception e) {
			throw new RiverException(e);
		}

		return _wrapper;
	}

	@Override
	public void close() {
		log.fine("Asking to the native reference collector for terminate");
		rc.terminate();
		log.fine("Cleaning the last objects in the phantom reference list: " + phantomWrapperMap.size());
		phantomWrapperMap.clear();
		log.fine("Cleaning the last objects in the cache: " + softWrapperMap.size());
		softWrapperMap.clear();
		log.info("Factory closed.");
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}