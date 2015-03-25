package org.riverframework;

import java.util.List;

public interface DocumentCollection<T> extends List<T> {
	public Database getDatabase();
	
	public DocumentCollection<T> deleteAll();
}
