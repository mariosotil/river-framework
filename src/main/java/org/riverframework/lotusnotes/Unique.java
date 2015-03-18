package org.riverframework.lotusnotes;

public interface Unique extends org.riverframework.Unique {
	@Override
	public org.riverframework.lotusnotes.Document setId(String id);

	@Override
	public org.riverframework.lotusnotes.Document generateId();
}
