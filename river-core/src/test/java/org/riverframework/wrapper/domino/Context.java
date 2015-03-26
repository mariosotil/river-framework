package org.riverframework.wrapper.domino;

import java.io.File;

import org.ini4j.Wini;
import org.riverframework.RiverException;

public final class Context {
	private static String localDatabase;
	private static String remoteDatabase;
	private static String server;

	static {
		try {
			String location = System.getProperty("user.home") + File.separator + ".river-framework" + File.separator
					+ "test-context";

			File f = new File(location);
			Wini credentials = new Wini(f);

			localDatabase = credentials.get("default", "local-database");
			remoteDatabase = credentials.get("default", "remote-database");
			server = credentials.get("default", "server");

		} catch (Exception e) {
			throw new RiverException(e);
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
