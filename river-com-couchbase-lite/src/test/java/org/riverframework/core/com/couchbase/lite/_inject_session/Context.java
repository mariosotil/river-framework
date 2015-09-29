package org.riverframework.core.com.couchbase.lite._inject_session;

import java.io.IOException;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.core.Session;

import com.couchbase.lite.JavaContext;
import com.couchbase.lite.Manager;

public final class Context extends org.riverframework.core.AbstractContext {
	@Override
	public String getConfigurationFileName() {
		return "";
	}

	@Override
	public Session getSession() {
		com.couchbase.lite.Manager _session = null;

		try {
			_session = new Manager(new JavaContext(), Manager.DEFAULT_OPTIONS);
		} catch (IOException e) {
			throw new RiverException(e);
		}
	
		Session session = River.getSession(River.COM_COUCHBASE_LITE, _session);
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.COM_COUCHBASE_LITE);
	}
}
