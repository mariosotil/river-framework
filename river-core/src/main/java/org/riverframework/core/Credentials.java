package org.riverframework.core;

import java.io.File;

import org.ini4j.Wini;

public final class Credentials {
	private static String username;
	private static String password;

	static {
		try {
			String location = System.getProperty("user.home") + File.separator + ".river-framework" + File.separator
					+ "credentials";

			Wini credentials = new Wini(new File(location));

			username = credentials.get("default", "username");
			password = credentials.get("default", "password");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}
}
