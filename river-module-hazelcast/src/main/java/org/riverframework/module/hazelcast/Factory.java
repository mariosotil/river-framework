package org.riverframework.module.hazelcast;

import org.riverframework.RiverException;
import org.riverframework.module.Session;
import org.riverframework.module.hazelcast.DefaultSession;


class Factory {
	@SuppressWarnings("unused")
	private static Session createSession(Object... parameters) {
		if (parameters.length == 1 && parameters[0] instanceof com.hazelcast.core.HazelcastInstance) {
			return new DefaultSession((com.hazelcast.core.HazelcastInstance) parameters[0]);
		}

		throw new RiverException(
				"Valid parameter: a com.hazelcast.core.HazelcastInstance object.");	
	}
}
