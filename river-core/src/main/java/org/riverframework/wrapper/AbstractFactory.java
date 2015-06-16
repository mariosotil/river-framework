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

	protected volatile org.riverframework.wrapper.Session<N> _session = null;
	protected Class<? extends AbstractNativeReference<N>> nativeReferenceClass = null;
	protected volatile ConcurrentHashMap<String, AbstractNativeReference<N>> weakWrapperMap = null;
	protected volatile ReferenceQueue<Base<N>> queue = null;

	private Constructor<?> constructorNativeReference = null;

	protected AbstractFactory(Class<? extends AbstractNativeReference<N>> nativeReferenceClass) {
		this.nativeReferenceClass = nativeReferenceClass;

		weakWrapperMap = new ConcurrentHashMap<String, AbstractNativeReference<N>>();
		queue = new ReferenceQueue<Base<N>>();

		try {
			constructorNativeReference = nativeReferenceClass.getDeclaredConstructor(Base.class, ReferenceQueue.class);
			constructorNativeReference.setAccessible(true);
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public void logStatus() {
		// TODO: ??
	}

	private <U extends Base<N>> U createWrapper(Class<U> outputClass, Class<? extends N> inputClass, Object __obj) {
		U _wrapper = null;

		try {
			// TODO: create a Constructor objects cache
			Constructor<?> constructor = outputClass.getDeclaredConstructor(org.riverframework.wrapper.Session.class, inputClass);
			constructor.setAccessible(true);
			_wrapper = outputClass.cast(constructor.newInstance(_session, __obj));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return _wrapper;
	}

	private void createReference(String id, Base<N> _wrapper) {
		try {
			AbstractNativeReference<N> ref = nativeReferenceClass.cast(constructorNativeReference.newInstance(_wrapper, queue));
			weakWrapperMap.put(id, ref);
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	private String calcIdFromNativeObject(Class<? extends Base<N>> outputClass, Class<? extends N> inputClass, Object __obj) {
		String id = null;
		try {
			Method methodCalcObjectId = outputClass.getDeclaredMethod("calcObjectId", inputClass);
			methodCalcObjectId.setAccessible(true);
			id = (String) methodCalcObjectId.invoke(null, __obj);
		} catch (Exception e) {
			throw new RiverException(e);
		}
		return id;
	}

	protected abstract boolean isValidNativeObject(N __native);

	@SuppressWarnings("unchecked")
	public <U extends Base<N>> U getWrapper(Class<U> outputClass, Class<? extends N> inputClass, Object __obj) {
		U _wrapper = null;
		String actionTaken = null;

		try {
			cleanUp();

			if (__obj == null) {
				// Null object pattern
				actionTaken = "Created a null object";
				_wrapper = createWrapper(outputClass, inputClass, null);
			} else {
				// Looking for the object in the cache
				String id = calcIdFromNativeObject(outputClass, inputClass, __obj);
				AbstractNativeReference<N> ref = nativeReferenceClass.cast(weakWrapperMap.get(id));

				if (ref == null) {
					// It's no registered in the cache
					actionTaken = "No registered. Creating the wrapper for the object";
					_wrapper = createWrapper(outputClass, inputClass, __obj);
					createReference(id, _wrapper);
				} else {
					// It's registered in the cache
					N __native = ref.getNativeObject();
					_wrapper = (U) ((WeakReference<Base<N>>) ref).get();

					if (isValidNativeObject(__native)) {
						// There's a valid native object

						if (_wrapper == null) {
							// There's no wrapper. We create a new one with the
							// old native object
							actionTaken = "Registered. Creating a wrapper for registered native object";
							_wrapper = createWrapper(outputClass, inputClass, __native);
							createReference(id, _wrapper);
						} else {
							// There's a wrapper
							if (isValidNativeObject(_wrapper.getNativeObject())) {
								// with a valid native object
								actionTaken = "Registered. Retrieving the wrapper and its native object from the cache";
							} else {
								// with an invalid native object.
								actionTaken = "Registered but closed. Creating the wrapper with the registered native object from the cache";
								_wrapper = createWrapper(outputClass, inputClass, __native);
								createReference(id, _wrapper);
							}
						}
					} else {
						// There's no a valid native object

						if (_wrapper == null) {
							// There's no wrapper. We create a new one with the
							// new native object
							actionTaken = "Registered. Creating the wrapper for the object";
							_wrapper = createWrapper(outputClass, inputClass, __obj);
							createReference(id, _wrapper);
						} else {
							// There's a wrapper
							actionTaken = "Registered. Replacing the object in an existent wrapper";
							_wrapper = createWrapper(outputClass, inputClass, __obj);
							createReference(id, _wrapper);
						}
					}
				}
			}

			if (log.isLoggable(Level.FINEST)) {
				StringBuilder sb = new StringBuilder();
				sb.append(actionTaken);
				sb.append(". id=");
				sb.append(_wrapper.getObjectId());
				sb.append(" wrapper=");
				sb.append(_wrapper.getClass().getName());
				sb.append(" (");
				sb.append(_wrapper.hashCode());
				sb.append(") native=");
				sb.append(__obj == null ? "<null>" : __obj.getClass().getName());
				sb.append(" (");
				sb.append(__obj == null ? "" : __obj.hashCode());
				sb.append(")");

				log.finest(sb.toString());
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return _wrapper;
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public void close() {
		log.fine("Cleaning the last objects in the cache: " + weakWrapperMap.size());
		weakWrapperMap.clear();
		log.info("Factory closed.");
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}