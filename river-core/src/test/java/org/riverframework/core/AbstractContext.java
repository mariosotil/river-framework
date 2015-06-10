package org.riverframework.core;

import java.io.File;

import org.ini4j.Wini;
import org.riverframework.RiverException;
import org.riverframework.Session;

public abstract class AbstractContext implements org.riverframework.Context {
	private String testDatabaseServer = "";
	private String testDatabasePath = "";
	private String remoteDatabaseServer = "";
	private String remoteDatabasePath = "";

	public AbstractContext() {
		// Exists only to defeat instantiation
		try {
			StringBuilder sb = new StringBuilder();

			sb.append(System.getProperty("user.home"));
			sb.append(File.separator);
			sb.append(".river-framework");
			sb.append(File.separator);
			sb.append(getConfigurationFileName());

			String location = sb.toString();

			Wini context = new Wini(new File(location));

			testDatabaseServer = context.get("default", "test-database-server");
			testDatabasePath = context.get("default", "test-database-path");
			remoteDatabaseServer = context.get("default", "remote-database-server");
			remoteDatabasePath = context.get("default", "remote-database-path");

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
