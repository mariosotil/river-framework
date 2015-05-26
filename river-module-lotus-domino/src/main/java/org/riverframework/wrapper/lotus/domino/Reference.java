package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.riverframework.River;
import org.riverframework.wrapper.Base;

public class Reference extends java.lang.ref.PhantomReference<org.riverframework.wrapper.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Object nativeObj = null;

	public Reference(Base wrapper, ReferenceQueue<? super Base> queue) {
		super(wrapper, queue);

		nativeObj = wrapper.getNativeObject();
	}

	public void close() {
		if (nativeObj != null) {
			int hc = nativeObj.hashCode();

			try {
				((lotus.domino.Base)this.nativeObj).recycle();
				log.finest("Recycled: " + hc);
			} catch (NotesException e) {
				log.warning("Exception after recycling object" + hc);
			}
		}
	}
}
