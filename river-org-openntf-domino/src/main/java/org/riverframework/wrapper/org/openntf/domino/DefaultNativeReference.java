package org.riverframework.wrapper.org.openntf.domino;

import java.lang.ref.ReferenceQueue;

import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Base;

class DefaultNativeReference extends AbstractNativeReference<org.openntf.domino.Base<?>> {
	// private static final Logger log = River.LOG_WRAPPER_ORG_OPENNTF_DOMINO;

	public DefaultNativeReference(Base<org.openntf.domino.Base<?>> referent,
			ReferenceQueue<Base<org.openntf.domino.Base<?>>> q) { 
		super(referent, q);
	}

	@Override
	public void close() {
		//There's no need to recycle the objects because the OpenNTF library does itself
	}
}

