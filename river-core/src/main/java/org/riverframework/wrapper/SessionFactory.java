package org.riverframework.wrapper;

import com.google.inject.name.Named;

public interface SessionFactory {
	@Named("Domino")
	Session createDomino(String... parameters);

	@Named("Openntf")
	Session createOpenntf(String... parameters);

	@Named("Hazelcast")
	Session createHazelcast(String... parameters);
}
