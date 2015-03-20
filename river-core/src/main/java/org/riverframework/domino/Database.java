package org.riverframework.domino;

public interface Database extends org.riverframework.Database {
	public org.riverframework.domino.Database open(org.openntf.domino.Database obj);

	@Override
	public org.riverframework.domino.Database open(String... location);

	@Override
	public org.riverframework.domino.Database getMainReplica();

	@Override
	public org.riverframework.domino.DocumentCollection getAllDocuments();

	@Override
	public org.riverframework.domino.Counter getCounter(String key);
}
