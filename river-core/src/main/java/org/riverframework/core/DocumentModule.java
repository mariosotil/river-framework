package org.riverframework.core;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class DocumentModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(org.riverframework.Document.class,
						Names.named("DefaultDocument"),
						org.riverframework.core.DefaultDocument.class)
				.build(DocumentFactory.class));
	}
}
