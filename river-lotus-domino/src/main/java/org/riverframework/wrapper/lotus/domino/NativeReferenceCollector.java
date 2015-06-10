package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.wrapper.Base;
import java.lang.ref.Reference;

class NativeReferenceCollector extends Thread implements Runnable {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected ReferenceQueue<Base> queue = null;
	protected LinkedList<Reference<? extends Base>> list = null;
	private volatile boolean running = true;

	public NativeReferenceCollector(ReferenceQueue<Base> queue, LinkedList<Reference<? extends Base>> list) {
		this.queue = queue;
		this.list = list;
		setName(this.getClass().getName());
		setDaemon(true);
	}

	public void terminate() {
		log.info("Closing Reference Collector");
		running = false;
	}

	@Override
	public void run() {
		log.info("Reference Collector started.");

		while (running) {
			try {
				Reference<? extends Base> ref = null;
				ref = queue.remove();
				list.remove(ref);
				NativeReference nat = ((org.riverframework.wrapper.lotus.domino.NativeReference) ref);
				String id = nat.getObjectId();
				log.finest("Removed Id=" + id);
				nat.cleanUp();
			} catch (Exception ex) {
				log.log(Level.WARNING, "Exception at recycling", ex);
			}
		}

		list.clear();
		log.info("Reference Collector closed.");
	}
}
