package org.riverframework;

import java.util.Iterator;

public interface DocumentCollection<T> extends Iterator<T> {
	@SuppressWarnings("rawtypes")
	public DocumentCollection removeAll();
}
