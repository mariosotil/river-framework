package org.riverframework.utils;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPool<K, V> {
	protected final Map<String, V> map = new HashMap<String, V>();
	
	public final V get(K key) {
		String id = calcId(key);
		V value = map.get(id);
		
		if (value == null) {
			value = calcValue(key);
			map.put(id, value);
		}
		
		return value;
	}

	protected abstract V calcValue(K key);
	
	protected abstract String calcId(K key);
}
