package org.riverframework.wrapper.hazelcast;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.hazelcast.DefaultDatabase;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class DefaultSession implements org.riverframework.wrapper.Session {
    private HazelcastInstance _instance = null;
	private UUID sessionUUID = null;

	public DefaultSession(com.hazelcast.core.HazelcastInstance _i) {
		_instance = _i;
		sessionUUID = UUID.randomUUID();
	}

	@Override
	public HazelcastInstance getNativeObject() {
		return _instance;
	}

	@Override
	public boolean isOpen() {
		return _instance != null;
	}

	@Override
	public Database getDatabase(String... location) {
		IMap<String, Map<String, Object>> _database = _instance.getMap(location[0]);
		Database database = new DefaultDatabase(_database);
		return database;
	}

	@Override
	public String getUserName() {
		return "(Anonymous)";
	}

	@Override
	public void close() {
		Collection<DistributedObject> instances =  _instance.getDistributedObjects();
		for(DistributedObject instance: instances){
			if ( instance.getServiceName().equals("hz:impl:mapService")){
				instance.destroy();
			}
		}
		
		_instance.getLifecycleService().shutdown();
		_instance = null;
	}

	@Override
	public String getObjectId() {
		return sessionUUID.toString();
	}
}
