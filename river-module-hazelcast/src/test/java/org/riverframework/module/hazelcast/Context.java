package org.riverframework.module.hazelcast;

import org.riverframework.River;
import org.riverframework.Session;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public final class Context extends org.riverframework.core.AbstractContext {
	private static com.hazelcast.core.HazelcastInstance _instance = null;

	@Override
	public String getConfigurationFileName() {
		return "test-configuration-org-openntf-domino";
	}

	@SuppressWarnings("deprecation")
	@Override
	public Session getSession() {
		System.setProperty("hazelcast.logging.type", "none");

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.addAddress("127.0.0.1:5701");
		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		
		Session session = River.getSession(River.MODULE_HAZELCAST, client);		
		return session;
	}

	@Override
	public void closeSession() {
		River.closeSession(River.MODULE_HAZELCAST);
	}

	public static void initServer() {
		Config cfg = null;

		cfg = new Config();
		cfg.setProperty("hazelcast.logging.type", "none");
		_instance = Hazelcast.newHazelcastInstance(cfg);
	}
	
	public static void stopServer() {
		_instance.shutdown();
	}
}
