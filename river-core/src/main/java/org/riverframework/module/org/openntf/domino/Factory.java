package org.riverframework.module.org.openntf.domino;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;

public class Factory {
	public static org.openntf.domino.Session createSession(String... parameters) {
		if (parameters.length != 3)
			throw new RiverException("There are needed three parameters in this order: server, username and password.");

		String server = parameters[0];
		String username = parameters[1];
		String password = parameters[2];

		try {
			return org.openntf.domino.utils.Factory
					.fromLotus(NotesFactory.createSession(server, username, password),
							org.openntf.domino.Session.class,
							null);
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}
}
