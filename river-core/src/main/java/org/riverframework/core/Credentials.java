package org.riverframework.core;

import java.io.File;

import org.ini4j.Wini;

/**
 * Allows creates a Session using the credentials stored in the file system. The file must be named as
 * $user_home\.river-framework\credentials and it must have this content:
 * 
 * <code>
 * [default]
 * server=SERVERNAME
 * username=USERNAME
 * password=PASSWORD
 * </code>
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class Credentials {
	private static String server;
	private static String username;
	private static String password;

	static {
		try {
			String location = System.getProperty("user.home") + File.separator + ".river-framework" + File.separator
					+ "credentials";

			Wini credentials = new Wini(new File(location));

			server = credentials.get("default", "server");
			username = credentials.get("default", "username");
			password = credentials.get("default", "password");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the server defined in the credentials file. It must be set at the section [default], key server 
	 * @return the server name
	 */
	public static String getServer() {
		return server;
	}

	/**
	 * Returns the user name defined in the credentials file. It must be set at the section [default], key username 
	 * @return the user name
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * Returns the password defined in the credentials file. It must be set at the section [default], key password 
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}
}
