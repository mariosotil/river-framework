package org.riverframework.module;

import org.riverframework.River;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class SessionModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(org.riverframework.module.Session.class,
						Names.named(River.MODULE_LOTUS_DOMINO),
						org.riverframework.module.lotus.domino.DefaultSession.class)
				.implement(org.riverframework.module.Session.class,
						Names.named(River.MODULE_ORG_OPENNTF_DOMINO),
						org.riverframework.module.org.openntf.domino.DefaultSession.class)
				.implement(org.riverframework.module.Session.class,
						Names.named(River.MODULE_HAZELCAST),
						org.riverframework.module.hazelcast.DefaultSession.class)
				.build(SessionFactory.class));
	}
}
