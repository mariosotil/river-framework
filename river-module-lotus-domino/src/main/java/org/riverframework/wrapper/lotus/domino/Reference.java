package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.wrapper.Base;

class Reference extends java.lang.ref.PhantomReference<org.riverframework.wrapper.Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Object __native = null;
	protected String id = null;

	public Reference(Base wrapper, ReferenceQueue<? super Base> queue) {
		super(wrapper, queue);

		id = wrapper.getObjectId();
		__native = wrapper.getNativeObject();
	}
	
	public String getObjectId() {
		return id;
	}

	public void close() {
		if (__native != null) {
			Class<?> clazz = __native.getClass();
			String wrapperClass = clazz.getName();
			int hc = __native.hashCode();

			try {
				try {
					Method method = clazz.getMethod("recycle");
					method.invoke(__native);
				} catch (Exception e) {
					log.log(Level.WARNING, "", e);
				}
				
				log.finest("Recycled: id=" + id + " wrapper=" + wrapperClass + " (" + hc + ")");
			} catch (Exception e) {
				log.warning("Exception after recycling object" + hc);
			} finally {
				__native = null;
			}
		}
	}
}
