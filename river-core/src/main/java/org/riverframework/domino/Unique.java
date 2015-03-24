package org.riverframework.domino;

public interface Unique extends org.riverframework.domino.Document, org.riverframework.Unique {
	@Override
	public org.riverframework.domino.Document setId(String id);

	@Override
	public org.riverframework.domino.Document generateId();
}
