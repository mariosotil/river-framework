package org.riverframework.domino;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class LocalContext {
	private static final String configuration = System.getProperty("user.home") +
			File.separator + "river_test_context.txt";
	private static String password;
	private static String database;

	static {
		try {
			Scanner sc = new Scanner(new File(configuration));

			password = sc.nextLine();
			database = sc.nextLine();

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getPassword() {
		return password;
	}

	public static String getDatabase() {
		return database;
	}
}
