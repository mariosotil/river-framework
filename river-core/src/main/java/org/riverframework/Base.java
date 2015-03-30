package org.riverframework;

public interface Base {
	public String getObjectId();

	public Base getParent();

	public Object getWrappedObject();

	public void close();
}
