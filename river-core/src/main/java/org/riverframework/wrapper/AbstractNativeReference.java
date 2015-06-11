package org.riverframework.wrapper;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public abstract class AbstractNativeReference<N> extends PhantomReference<Base<N>> implements NativeReference<N> {

	protected volatile N __native = null;
	protected String id = null;

	public AbstractNativeReference(Base<N> referent, ReferenceQueue<Base<N>> q) {
		super(referent, q);

		__native = referent.getNativeObject();
		id = referent.getObjectId();
	}

	@Override
	public final N getNativeObject() {
		return __native;
	}

	@Override
	public final String getObjectId() {
		return id;
	}

	@Override
	public final boolean isOpen() {
		return __native != null;
	}

	@Override
	public void close() {

	}
}
