package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.riverframework.River;
import org.riverframework.wrapper.Base;
import java.lang.ref.PhantomReference;

class NativeReference extends PhantomReference<Base> {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Object __native = null;
	protected String id = null;

	public NativeReference(Base wrapper, ReferenceQueue<? super Base> queue) {
		super(wrapper, queue);

		id = wrapper.getObjectId();
		__native = wrapper.getNativeObject();
	}

	public String getObjectId() {
		return id;
	}

	synchronized public void cleanUp() {
		if (__native != null) {
			Class<?> clazz = __native.getClass();
			String wrapperClass = clazz.getName();
			//int hc = __native.

			try {
				Method method = clazz.getMethod("recycle");
				method.invoke(__native);
			} catch (Exception e) {
				log.log(Level.WARNING, "Exception while recycling object " + id, e);
			} finally {
				__native = null;
			}

			log.finest("Recycled: id=" + id + " wrapper=" + wrapperClass); //+ " (" + hc + ")");
		}
	}
}
