package org.riverframework;

public interface Context {
	public String getTestDatabaseServer();

	public String getTestDatabasePath();

	public String getRemoteDatabaseServer();

	public String getRemoteDatabasePath();

	public String getConfigurationFileName();

	public Session getSession();

	public void closeSession();
}
