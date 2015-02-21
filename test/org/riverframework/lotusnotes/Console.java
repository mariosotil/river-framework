package org.riverframework.lotusnotes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import lotus.domino.NotesThread;

public class Console extends NotesThread {
	public static void main(String argv[]) {
		Console t = new Console(argv);
		t.start();
	}

	public Console(String argv[]) {

	}

	private static String join(Collection<?> s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator<?> iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	@Override
	public void runNotes() {
		try {

			DefaultSession session = DefaultSession
					.getInstance()
					.open(Credentials.getServer(), Credentials.getUser(), Credentials.getPassword());

			System.out.println("PATH="
					+ join(new ArrayList<String>(Arrays.asList(System.getProperty("java.library.path")
							.split(";"))), "\n"));
			System.out.println("Current user name "
					+ session.getNotesSession().getCommonUserName());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
