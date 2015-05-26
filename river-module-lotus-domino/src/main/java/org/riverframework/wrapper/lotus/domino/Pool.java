package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.wrapper.Base;

public class Pool {
	protected final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	protected ConcurrentHashMap<String, WeakReference<Base>> map = null;
	protected LinkedList<Reference> list = null;
	protected ReferenceQueue<Base> queue = null;
	private static Pool instance = null;

	protected Pool() {
		map = new ConcurrentHashMap<String, WeakReference<Base>>();
		list = new LinkedList<Reference>();
		queue = new ReferenceQueue<Base>();

		Thread referenceThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Reference ref = (Reference) queue.remove();
						ref.close();
						list.remove(ref);
					} catch (Exception ex) {
						log.warning("Exception at recycling");
					}
				}
			}
		};
		referenceThread.setDaemon(true);
		referenceThread.start();
	}

	// Lazy Initialization (If required then only)
	public static Pool getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (Pool.class) {
				if (instance == null) {
					instance = new Pool();
				}
			}
		}
		return instance;
	}

	synchronized public void checkObj(Base wrapper) {
		if (wrapper != null && wrapper.isOpen()) {
			String id = wrapper.getObjectId();
			WeakReference<Base> ref = map.get(id);
			if (ref == null) {
				if (log.isLoggable(Level.FINEST)) {
					Object nativeObject = wrapper.getNativeObject();
					log.finest("Registering object: id=" + id + "wrapper=" + wrapper.getClass().getName() + " (" + wrapper.hashCode()
							+ ") native=" + nativeObject.getClass().getName() + " (" + nativeObject.hashCode() + ")");
				}

				map.put(id, new WeakReference<Base>(wrapper));
				list.add(new Reference(wrapper, queue));
			} else {
				Base old = wrapper;
				wrapper = ref.get();

				if (log.isLoggable(Level.FINEST)) {
					log.finest("Changing wrapper for the registered one: id=" + id + " wrapper=" + wrapper.getClass().getName() + " ("
							+ wrapper.hashCode() + ")  Destroying old=" + old.getClass().getName() + " (" + old.hashCode() + ")");
				}

				old = null;
			}
		}
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
						try {							
							nativeObj.recycle();
							log.finest("Recycled: " + hc);
						} catch (NotesException e) {
							log.warning("Exception after recycling object" + hc);
						}

						nativeObj = null;
					}

					wrapper = null;
				} 
			}
		}
	}
}