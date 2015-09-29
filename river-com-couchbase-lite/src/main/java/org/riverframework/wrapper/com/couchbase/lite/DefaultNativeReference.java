package org.riverframework.wrapper.com.couchbase.lite;

import java.lang.ref.ReferenceQueue;

import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Base;

class DefaultNativeReference extends AbstractNativeReference<Object> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	public DefaultNativeReference(Base<Object> referent,
			ReferenceQueue<Base<Object>> q) { 
		super(referent, q);
	}

	@Override
	public void close() {
		//There's no need to recycle the objects because the OpenNTF library does itself
	}
}

