package org.riverframework.wrapper;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;

public abstract class AbstractNativeReferenceCollector<N> extends Thread implements NativeReferenceCollector {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Class<? extends NativeReference<N>> nativeReferenceClass = null;
	protected volatile ConcurrentHashMap<String, WeakReference<? extends Base<N>>> weakWrapperMap = null;
	protected volatile ReferenceQueue<Base<N>> queue = null;
	private volatile boolean running = true;
	private final int MAX_DEATH_ROW_SIZE = 2000;

	public AbstractNativeReferenceCollector(Class<? extends NativeReference<N>> nativeReferenceClass,
			ConcurrentHashMap<String, WeakReference<? extends Base<N>>> weakWrapperMap, ReferenceQueue<Base<N>> queue) {
		this.nativeReferenceClass = nativeReferenceClass;
		this.weakWrapperMap = weakWrapperMap;
		this.queue = queue;
		setName(this.getClass().getName());
		// setDaemon(true);
	}

	@Override
	public void terminate() {
		log.info("Closing Reference Collector");
		running = false;
	}

	@Override
	public void run() {
		log.info("Reference Collector started.");

		LinkedList<Reference<? extends Base<N>>> deathRow = new LinkedList<Reference<? extends Base<N>>>();
		int i = 0;

		while (running) {
			try {
				Thread.sleep(1000);
				Reference<? extends Base<N>> prisoner = queue.remove();
				if (prisoner != null) {
					deathRow.add(prisoner);

					if (deathRow.size() > MAX_DEATH_ROW_SIZE) {
						log.finest("Prison full! Round=" + i++ + " size=" + deathRow.size());
						Reference<? extends Base<N>> ref = deathRow.removeFirst();
						NativeReference<N> nat = nativeReferenceClass.cast(ref);
						String id = nat.getObjectId();

						log.finest("Recycling: id=" + id + " native=" + nat.getNativeObject().getClass().getName() + " ("
								+ nat.getNativeObject().hashCode() + ")");
						weakWrapperMap.remove(id);
						nat.close();
					}
				}
			} catch (Exception ex) {
				log.log(Level.WARNING, "Exception at recycling", ex);
			}
		}

		log.info("Reference Collector closed.");
	}

	@Override
	public void logStatus() {
		log.finest("list=" + weakWrapperMap.size());
	}
}
