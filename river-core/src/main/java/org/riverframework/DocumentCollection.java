package org.riverframework;

import java.util.Iterator;

public interface DocumentCollection<T> extends Iterator<T> {
	public Database getDatabase();
	
	@SuppressWarnings("rawtypes")
	public DocumentCollection removeAll();
}
