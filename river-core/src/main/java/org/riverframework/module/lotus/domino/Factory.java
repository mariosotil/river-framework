package org.riverframework.module.lotus.domino;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.module.Session;

class Factory {
	@SuppressWarnings("unused")
	private static Session createSession(String... parameters) {
		if (parameters.length != 3)
			throw new RiverException("There are needed three parameters in this order: server, username and password.");

		try {
			return new DefaultSession(NotesFactory.createSession(parameters[0], parameters[1], parameters[2]));
		} catch (NotesException e) {
			throw new RiverException(e);
		}
	}
}
