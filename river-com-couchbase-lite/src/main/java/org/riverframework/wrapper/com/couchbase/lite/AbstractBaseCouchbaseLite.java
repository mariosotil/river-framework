package org.riverframework.wrapper.com.couchbase.lite;

import org.riverframework.wrapper.AbstractBase;
import org.riverframework.wrapper.Session;

abstract class AbstractBaseCouchbaseLite<N> extends AbstractBase<N, com.couchbase.lite.Manager, Object> {

	protected AbstractBaseCouchbaseLite(Session<com.couchbase.lite.Manager> _session, N __native) {
		super(_session, __native);
	}

}
