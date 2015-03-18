package org.riverframework;

public interface Unique {
	public Document generateId();

	public Document setId(String id);

	public String getId();
}
