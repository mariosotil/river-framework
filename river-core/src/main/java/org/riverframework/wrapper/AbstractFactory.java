package org.riverframework.wrapper;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.RiverException;

public abstract class AbstractFactory<N> implements org.riverframework.wrapper.Factory<N> {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Class<? extends NativeReference<N>> nativeReferenceClass = null;
	protected Class<? extends NativeReferenceCollector> nativeReferenceCollectorClass = null;

	protected volatile ConcurrentHashMap<String, NativeReference<N>> weakWrapperMap = null;
	protected volatile ReferenceQueue<Base<N>> queue = null;

	protected volatile org.riverframework.wrapper.Session<N> _session = null;

	protected NativeReferenceCollector rc = null;

	protected AbstractFactory(Class<? extends NativeReference<N>> nativeReferenceClass,
			Class<? extends NativeReferenceCollector> nativeReferenceCollectorClass) {
		this.nativeReferenceClass = nativeReferenceClass;
		this.nativeReferenceCollectorClass = nativeReferenceCollectorClass;

		weakWrapperMap = new ConcurrentHashMap<String, NativeReference<N>>();
		queue = new ReferenceQueue<Base<N>>();

		try {
			Constructor<?> constructor = nativeReferenceCollectorClass.getDeclaredConstructor(Class.class, ConcurrentHashMap.class,
					ReferenceQueue.class);
			constructor.setAccessible(true);
			rc = nativeReferenceCollectorClass.cast(constructor.newInstance(nativeReferenceClass, weakWrapperMap, queue));
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

		try {
			Constructor<?> constructor = nativeReferenceClass.getDeclaredConstructor(Base.class, ReferenceQueue.class);
			constructor.setAccessible(true);
			NativeReference<N> ref = nativeReferenceClass.cast(constructor.newInstance(_wrapper, queue));
			weakWrapperMap.put(id, ref);
		} catch (Exception e) {
			throw new RiverException(e);
		}

	}

	protected Base<N> retrieveFromCache(String id) {
		Base<N> _wrapper = null;

		@SuppressWarnings("unchecked")
		WeakReference<Base<N>> ref = (WeakReference<Base<N>>) weakWrapperMap.get(id);

		if (ref != null) {
			// It's already registered
			log.finest("Already registered. id=" + id);
			_wrapper = ref.get();

			if (_wrapper == null) {
				// Removing the id from the cache
				log.finest("Wrapper is null. id=" + id);
				weakWrapperMap.remove(id);
			} else if (_wrapper.getNativeObject() == null) {
				// Removing the id from the cache, and freeing the weak and
				// phantom references
				log.finest("Native object is null. id=" + id);
				weakWrapperMap.remove(id);
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
		log.finest("cache=" + weakWrapperMap.size());
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
				try {
					_wrapper = outputClass.cast(constructor.newInstance(_session, __obj));
				} catch (InvocationTargetException e) {
					throw new RiverException(e);
				}

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
		// while (((Thread) rc).isAlive()) {
		// }
		log.fine("Cleaning the last objects in the cache: " + weakWrapperMap.size());
		weakWrapperMap.clear();
		log.info("Factory closed.");
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}