package org.riverframework.wrapper;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.RiverException;

public abstract class AbstractFactory<N> implements org.riverframework.wrapper.Factory<N> {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Class<? extends AbstractNativeReference<N>> nativeReferenceClass = null;
	protected volatile org.riverframework.wrapper.Session<?> _session = null;
	protected volatile Map<String, AbstractNativeReference<N>> wrapperMap = null;
	protected volatile ReferenceQueue<Base<N>> wrapperQueue = null;

	private Constructor<?> constructorNativeReference = null;

	protected AbstractFactory(Class<? extends AbstractNativeReference<N>> nativeReferenceClass) {
		this.nativeReferenceClass = nativeReferenceClass;

		wrapperMap = new ConcurrentHashMap<String, AbstractNativeReference<N>>();
		wrapperQueue = new ReferenceQueue<Base<N>>();

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

	protected void registerReference(String id, Base<?> _wrapper) {
		try {
			AbstractNativeReference<N> ref = nativeReferenceClass.cast(constructorNativeReference.newInstance(_wrapper,
					wrapperQueue));
			wrapperMap.put(id, ref);
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	protected abstract boolean isValidNativeObject(N __native);

	@Override
	public abstract void cleanUp(Base<? extends N>... except);

	protected <U extends Base<?>> U createWrapper(Class<U> outputClass, Class<? extends N> inputClass, N __obj) {
		U _wrapper = null;

		try {
			Constructor<?> constructor = outputClass.getDeclaredConstructor(org.riverframework.wrapper.Session.class,
					inputClass);
			constructor.setAccessible(true);

			_wrapper = outputClass.cast(constructor.newInstance(_session, __obj));

		} catch (Exception e) {
			throw new RiverException(e);
		}

		return _wrapper;
	}

	@SuppressWarnings("unchecked")
	protected <U extends Base<?>> U getWrapper(Class<U> outputClass, Class<? extends N> inputClass, String __obj) {
		U _wrapper = null;
		String actionTaken = null;

		try {
			// Retrieving directly from the cache
			String id = __obj;
			AbstractNativeReference<N> ref = nativeReferenceClass.cast(wrapperMap.get(id));

			if (ref == null) {
				_wrapper = createWrapper(outputClass, inputClass, null);
			} else {
				_wrapper = (U) ref.get();

				if (_wrapper == null) {
					_wrapper = createWrapper(outputClass, inputClass, null);
				} else {
					N __native = (N) _wrapper.getNativeObject();

					if (isValidNativeObject(__native)) {
						// Do nothing. Return the found wrapper.

					} else {
						// There's no a valid native object
						_wrapper = createWrapper(outputClass, inputClass, null);
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

	@SuppressWarnings("unchecked")
	protected <U extends Base<?>> U getWrapper(Class<U> outputClass, Class<? extends N> inputClass, N __obj) {
		U _wrapper = null;
		String actionTaken = null;

		try {
			if (__obj == null) {
				// Null object pattern
				actionTaken = "Created a null object";
				_wrapper = createWrapper(outputClass, inputClass, null);

			} else {
				log.finest("Getting a wrapper for the native object " + __obj.getClass().getName() + " hc="
						+ __obj.hashCode());

				_wrapper = createWrapper(outputClass, inputClass, __obj);

				// Clean up the reference map, except for the just created _wrapper object
				cleanUp((Base<N>) _wrapper);

				// Looking for the object in the cache
				String id = _wrapper.getObjectId();
				AbstractNativeReference<N> ref = nativeReferenceClass.cast(wrapperMap.get(id));

				if (ref == null) {
					// It's no registered in the cache
					actionTaken = "No registered. Creating the wrapper for the object";
					registerReference(id, _wrapper);
				} else {
					// It's registered in the cache
					N __native = ref.getNativeObject();
					U _oldWrapper = (U) ref.get();

					if (isValidNativeObject(__native)) {
						// There's a valid native object

						if (_oldWrapper == null) {
							// There's no wrapper. We create a new one with the
							// old native object
							actionTaken = "Registered. Creating a wrapper for registered native object";
							registerReference(id, _wrapper);
						} else {
							// There's a wrapper
							if (isValidNativeObject((N) _oldWrapper.getNativeObject())) {
								// with a valid native object
								actionTaken = "Registered. Retrieving the wrapper and its native object from the cache";
								_wrapper = _oldWrapper;
							} else {
								// with an invalid native object.
								actionTaken = "Registered but closed. Creating the wrapper with the registered native object from the cache";
								registerReference(id, _wrapper);
							}
						}
					} else {
						// There's no a valid native object

						if (_oldWrapper == null) {
							// There's no wrapper. We create a new one with the
							// new native object
							actionTaken = "Registered. Creating the wrapper for the object";
							registerReference(id, _wrapper);
						} else {
							// There's a wrapper
							actionTaken = "Registered. Replacing the object in an existent wrapper";
							// _wrapper = _oldWrapper;
							registerReference(id, _wrapper);
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
	public void close() {
		log.fine("Cleaning the last objects in the cache: " + wrapperMap.size());
		wrapperMap.clear();
		log.info("Factory closed.");
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}