package org.riverframework.lotusnotes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class Credentials {
	private static String server;
	private static String user;
	private static String password;

	static {
		try {
			Scanner sc = new Scanner(new File("d:\\river_credentials.txt"));

			server = sc.nextLine();
			user = sc.nextLine();
			password = sc.nextLine();

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static String getServer() {
		return server;
	}

	public static String getUser() {
		return user;
	}

	public static String getPassword() {
		return password;
	}
}
