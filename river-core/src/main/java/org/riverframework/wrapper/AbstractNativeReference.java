package org.riverframework.wrapper;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public abstract class AbstractNativeReference<N> extends WeakReference<Base<N>> {

	protected volatile N __native = null;
	protected String id = null;

	public AbstractNativeReference(Base<N> referent, ReferenceQueue<Base<N>> q) {
		super(referent, q);

		__native = referent.getNativeObject();
		id = referent.getObjectId();
	}

	public final N getNativeObject() {
		return __native;
	}

	public final String getObjectId() {
		return id;
	}

	abstract public void close();
}
