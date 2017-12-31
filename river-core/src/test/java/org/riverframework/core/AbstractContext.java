package org.riverframework.core;

import java.io.File;

import org.ini4j.Wini;
import org.riverframework.RiverException;

public abstract class AbstractContext implements org.riverframework.Context {
	private String testDatabaseServer = "";
	private String testDatabasePath = "";
	private String remoteDatabaseServer = "";
	private String remoteDatabasePath = "";

	public AbstractContext() {
		// Exists only to defeat instantiation
		try {
			testDatabaseServer = "test-database-server";
			testDatabasePath = "test-database-path";
			remoteDatabaseServer = "remote-database-server";
			remoteDatabasePath = "remote-database-path";

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public String getTestDatabaseServer() {
		return testDatabaseServer;
	}

	@Override
	public String getTestDatabasePath() {
		return testDatabasePath;
	}

	@Override
	public String getRemoteDatabaseServer() {
		return remoteDatabaseServer;
	}

	@Override
	public String getRemoteDatabasePath() {
		return remoteDatabasePath;
	}

	@Override
	public abstract Session getSession();

	@Override
	public abstract String getConfigurationFileName();
}
