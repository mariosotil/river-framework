package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.wrapper.Base;

class ReferenceCollector extends Thread implements Runnable {
	protected static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected ReferenceQueue<Base> queue = null;
	private volatile boolean running = true;

	public ReferenceCollector(ReferenceQueue<Base> queue) {
		this.queue = queue;
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
				Reference ref = null;
				do {
					synchronized(DefaultFactory.class) {
						ref = (Reference) queue.remove(100);
					}
					if (ref != null) {
						ref.close();
						//list.remove(ref); // <== This line breaks the process ò_ó!!!							
					}
				} while (ref != null);
			} catch (Exception ex) {
				log.log(Level.WARNING, "Exception at recycling", ex);
			}
		}
		log.info("Reference Collector closed.");
	}
}
