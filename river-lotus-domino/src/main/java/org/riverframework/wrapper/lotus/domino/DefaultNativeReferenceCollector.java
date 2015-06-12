package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

import org.riverframework.wrapper.AbstractNativeReferenceCollector;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.NativeReference;

class DefaultNativeReferenceCollector extends AbstractNativeReferenceCollector<lotus.domino.Base> {
	public DefaultNativeReferenceCollector(Class<? extends NativeReference<lotus.domino.Base>> nativeReferenceClass,
			ConcurrentHashMap<String, WeakReference<? extends Base<lotus.domino.Base>>> softWrapperMap,  
			ReferenceQueue<Base<lotus.domino.Base>> queue) {
		super(nativeReferenceClass, softWrapperMap, queue);
	}
}
