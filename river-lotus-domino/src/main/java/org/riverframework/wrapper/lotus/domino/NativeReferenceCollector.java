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

//		LinkedList<Reference<? extends Base>> deathRow = new LinkedList<Reference<? extends Base>>();
//		Reference<? extends Base> prisoner = null;

		while (running) {
			try {
				Reference<? extends Base> ref = null;
				ref = queue.remove();
				list.remove(ref);
				NativeReference nat = ((org.riverframework.wrapper.lotus.domino.NativeReference) ref);
				String id = nat.getObjectId();
				log.finest("Removed Id=" + id);
				// nat.cleanUp();

//				log.finest("In the death row Id=" + id);
//				deathRow.add(ref);
//				if (deathRow.size() > 1) {
//					prisoner = deathRow.removeFirst();
//					NativeReference nat2 = ((org.riverframework.wrapper.lotus.domino.NativeReference) prisoner);
//					String id2 = nat2.getObjectId();
//					log.finest("Executing Id=" + id2);
//					list.remove(prisoner);
//					log.finest("Recycling Id=" + id2);
//					nat2.cleanUp();
//					log.finest("Round finished XXX");
//				}
			} catch (Exception ex) {
				log.log(Level.WARNING, "Exception at recycling", ex);
			}
		}

		//deathRow.clear();
		list.clear();
		log.info("Reference Collector closed.");
	}
}
