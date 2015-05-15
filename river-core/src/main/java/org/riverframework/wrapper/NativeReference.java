package org.riverframework.wrapper;

import java.lang.ref.WeakReference;

public class NativeReference<T> {
	private T nativeObject;
	private WeakReference<org.riverframework.wrapper.Base> wrapperRef;

	@SuppressWarnings("unchecked")
	protected NativeReference(org.riverframework.wrapper.Base wrapperObject) {
		this.nativeObject = (T) wrapperObject.getNativeObject();
		this.wrapperRef = new WeakReference<org.riverframework.wrapper.Base>(
				wrapperObject);
	}

	public T getNativeObject() {
		return nativeObject;
	}

	public org.riverframework.wrapper.Base getWrapperObject() {
		return wrapperRef.get();
	}
}
