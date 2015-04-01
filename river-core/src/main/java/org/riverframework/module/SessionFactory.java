package org.riverframework.module;

import org.riverframework.River;

import com.google.inject.name.Named;

public interface SessionFactory {
	@Named(River.MODULE_LOTUS_DOMINO)
	Session create(lotus.domino.Session obj);

	@Named(River.MODULE_ORG_OPENNTF_DOMINO)
	Session create(org.openntf.domino.Session obj);

	@Named(River.MODULE_HAZELCAST)
	Session create(org.riverframework.module.hazelcast.MockSession obj);
}
