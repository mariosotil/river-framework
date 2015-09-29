package org.riverframework.core.com.couchbase.lite._local;

import org.riverframework.River;
import org.riverframework.core.Session;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "";
	}

	@Override
	public Session getSession() {
		Session session = River.getSession(River.COM_COUCHBASE_LITE);
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.COM_COUCHBASE_LITE);
	}
}
