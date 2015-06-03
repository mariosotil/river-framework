package org.riverframework.wrapper.org.openntf.domino;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

class DefaultFactory implements org.riverframework.wrapper.Factory {
	@SuppressWarnings("unused")
	private static Session getSession(Object... parameters) {
		if (parameters.length == 1 && parameters[0] instanceof lotus.domino.Session) {
			return new DefaultSession((org.openntf.domino.Session) org.openntf.domino.utils.Factory
					.fromLotus((lotus.domino.Session) parameters[0], org.openntf.domino.Session.class, null));
		}

		if (parameters.length == 0)
			try {
				return new DefaultSession((org.openntf.domino.Session) org.openntf.domino.utils.Factory
						.fromLotus(NotesFactory.createSession(), org.openntf.domino.Session.class, null));
			} catch (NotesException e) {
				throw new RiverException(e);
			}

		if (parameters.length == 3 && parameters[2] instanceof String)
			try {
				return new DefaultSession((org.openntf.domino.Session) org.openntf.domino.utils.Factory
						.fromLotus(NotesFactory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]),
								org.openntf.domino.Session.class, null));
			} catch (NotesException e) {
				throw new RiverException(e);
			}

		throw new RiverException(
				"Valid parameters: (A) one lotus.domino.Session, or (B) three Strings in this order: server, username and password.");
	}

	@Override
	public Database getDatabase(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getDocument(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getView(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentIterator getDocumentIterator(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
