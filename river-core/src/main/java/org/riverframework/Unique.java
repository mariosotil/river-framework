package org.riverframework;

public interface Unique extends Document {
	public Document generateId();

	public Document setId(String id);

	public String getId();
}
