package org.riverframework.utils;

import java.util.HashMap;

import org.riverframework.RiverException;

public class Tree<K,V> {
	private class Node extends HashMap<K, Node> {
		private static final long serialVersionUID = 2653098758100050927L;
		private V value = null;
		
		public void setValue(V value) {
			this.value = value;			
		}
		
		public V getValue() {
			return value;
		}		
	}
	
	private Node root = new Node();
	private Class<K> keyClass = null;
	private Class<V> valueClass = null;
	
	public Tree(Class<K> keyClass, Class<V> valueClass) {
		this.keyClass = keyClass;
		this.valueClass = valueClass;
	}
	
	@SuppressWarnings("unchecked")
	public Tree<K,V> put(Object... objects) {
		if (objects.length <= 1) 
			throw new RiverException("Expected at least two parameters: key and value.");
		
		Node nav = root;
		
		if (!objects[objects.length-1].getClass().isAssignableFrom(valueClass)) {
			throw new RiverException("The last parameter, the value, must be an instance of class " + valueClass.getName());
		}

		for(int i = 0; i < objects.length-1; i++) {
			if (!objects[i].getClass().isAssignableFrom(keyClass)) {
				throw new RiverException("The key must be an instance of class " + keyClass.getName());
			}
			Node next = nav.get(objects[i]);
						
			if (next == null) {
				next = new Node();
				nav.put((K) objects[i], next);
			}
			
			nav = next;
		} 
		
		nav.setValue((V) objects[objects.length-1]);
		
		return this;
	}
	
	public V get(K... keys) {
		Node nav = root;
		
		for(int i = 0; i < keys.length; i++) {
			nav = nav.get(keys[i]);
						
			if (nav == null) return null;
		} 
		
		return nav.getValue();
	}
}
