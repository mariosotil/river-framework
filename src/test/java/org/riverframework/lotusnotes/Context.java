package org.riverframework.lotusnotes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class Context {
	private static final String configuration = System.getProperty("user.home") + 
			File.separator + "river_test_context.txt";
	private static String server;
	private static String port;
	private static String user;
	private static String password;
	private static String database;
	private static boolean remote;

	static {
		try {
			Scanner sc = new Scanner(new File(configuration));

			server = sc.nextLine();
			port = sc.nextLine();
			user = sc.nextLine();
			password = sc.nextLine();
			database = sc.nextLine();

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Check if the
		Class<?> clazz = null;
		try {
			clazz = Class.forName("lotus.domino.local.Session");
		} catch (ClassNotFoundException e) {
			// Do nothing
		} finally {
			remote = (clazz == null);
		}
	}

	public static String getServer() {
		return server;
	}

	public static String getPort() {
		return port;
	}

	public static String getServerAndPort() {
		StringBuilder sb = new StringBuilder();
		sb.append(server);
		if(!port.equals("")) {
			sb.append(":");
			sb.append(port);
		}
		return sb.toString();
	}

	public static String getUser() {
		return user;
	}

	public static String getPassword() {
		return password;
	}

	public static String getDatabase() {
		return database;
	}

	public static boolean isRemote() {
		return remote;
	}
}
