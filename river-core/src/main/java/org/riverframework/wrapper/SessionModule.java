package org.riverframework.wrapper;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class SessionModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(org.riverframework.wrapper.Session.class,
						Names.named("Domino"),
						org.riverframework.wrapper.domino.DefaultSession.class)
				.implement(org.riverframework.wrapper.Session.class,
						Names.named("Openntf"),
						org.riverframework.wrapper.openntf.DefaultSession.class)
				.build(SessionFactory.class));
	}
}
