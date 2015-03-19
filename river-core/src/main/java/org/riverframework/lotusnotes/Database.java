package org.riverframework.lotusnotes;

public interface Database extends org.riverframework.Database {
	public org.riverframework.lotusnotes.Database open(lotus.domino.Database obj);

	@Override
	public org.riverframework.lotusnotes.Database open(String... location);

	@Override
	public org.riverframework.lotusnotes.Database getMainReplica();

	@Override
	public org.riverframework.lotusnotes.DocumentCollection getAllDocuments();

	@Override
	public org.riverframework.lotusnotes.Counter getCounter(String key);
}
