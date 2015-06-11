package org.riverframework.wrapper;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;

public abstract class AbstractNativeReferenceCollector<N> extends Thread implements NativeReferenceCollector {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected volatile ConcurrentHashMap<String, WeakReference<? extends Base<N>>> softWrapperMap = null;
	protected volatile ConcurrentHashMap<String, PhantomReference<Base<N>>> phantomWrapperMap = null;
	protected volatile ReferenceQueue<Base<N>> queue = null;
	protected Class<? extends NativeReference<N>> nativeReferenceClass = null;
	private volatile boolean running = true;

	public AbstractNativeReferenceCollector(Class<? extends NativeReference<N>> nativeReferenceClass,
			ConcurrentHashMap<String, WeakReference<? extends Base<N>>> softWrapperMap,
			ConcurrentHashMap<String, PhantomReference<Base<N>>> phantomWrapperMap, ReferenceQueue<Base<N>> queue) { // WeakReference<?
		// extends
		this.nativeReferenceClass = nativeReferenceClass;
		this.softWrapperMap = softWrapperMap;
		this.phantomWrapperMap = phantomWrapperMap;
		this.queue = queue;
		setName(this.getClass().getName());
		setDaemon(true);
	}

	@Override
	public void terminate() {
		log.info("Closing Reference Collector");
		running = false;
	}

	@Override
	public void run() {
		log.info("Reference Collector started.");

		while (running) {
			try {
				Reference<? extends Base<N>> ref = queue.remove();
				NativeReference<N> nat = nativeReferenceClass.cast(ref);
				String id = nat.getObjectId();

				WeakReference<? extends Base<N>> weak = softWrapperMap.get(id);
				if (weak.get() == null) {
					log.finest("Recycling: id=" + id + " native=" + nat.getNativeObject().getClass().getName() + " ("
							+ nat.getNativeObject().hashCode() + ")");
					softWrapperMap.remove(id);
					phantomWrapperMap.remove(id);
					nat.close();
				} else {
					log.finest("Forgiving: id=" + id + " native=" + nat.getNativeObject().getClass().getName() + " ("
							+ nat.getNativeObject().hashCode() + ")");
				}
			} catch (Exception ex) {
				log.log(Level.WARNING, "Exception at recycling", ex);
			}
		}

		// phantomWrapperMap.clear();
		log.info("Reference Collector closed.");
	}

	@Override
	public void logStatus() {
		log.finest("list=" + phantomWrapperMap.size());
	}
}
