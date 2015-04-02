package org.riverframework.module.lotus.domino;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.module.Session;

class Factory {
	@SuppressWarnings("unused")
	private static Session createSession(Object... parameters) {
		if (parameters.length == 1 && parameters[0] instanceof lotus.domino.Session) {
			return new DefaultSession((lotus.domino.Session) parameters[0]);
		}

		if (parameters.length == 0)
			try {
				return new DefaultSession(NotesFactory.createSession());
			} catch (NotesException e) {
				throw new RiverException(e);
			}

		if (parameters.length == 3 && parameters[2] instanceof String)
			try {
				return new DefaultSession(
						NotesFactory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]));
			} catch (NotesException e) {
				throw new RiverException(e);
			}

		throw new RiverException(
				"Valid parameters: one lotus.domino.Session, zero Objects or three Strings in this order: server, username and password.");
	}
}
