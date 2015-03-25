package org.riverframework.domino;

import java.io.File;

import org.ini4j.Wini;

public final class Context {
	private static String localDatabase;
	private static String remoteDatabase;
	private static String server;

	static {
		try {
			String location = System.getProperty("user.home") + File.separator + ".river-framework" + File.separator
					+ "test-context";

			Wini credentials = new Wini(new File(location));

			localDatabase = credentials.get("default", "local-database");
			remoteDatabase = credentials.get("default", "remote-database");
			server = credentials.get("default", "server");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDatabase() {
		return localDatabase;
	}
	
	public static String getServer() {
		return server;
	}

	public static String getRemoteDatabase() {
		return remoteDatabase;
	}	
}
