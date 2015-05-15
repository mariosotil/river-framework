package org.riverframework.wrapper.lotus.domino;

import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

class Factory {
	private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;
	private static DominoReferenceMap pool = null;

	static {
		pool = new DominoReferenceMap();
	}

	@SuppressWarnings("unused")
	private static Session createSession(Object... parameters) {
		if (parameters.length == 1 && parameters[0] instanceof lotus.domino.Session) {
			log.finer("Creating a session with one lotus.domino.Session parameter");
			return new DefaultSession((lotus.domino.Session) parameters[0]);
		}

		if (parameters.length == 3 && parameters[2] instanceof String) {
			log.finer("Creating a session with three parameters: server, username, password");
			try {
				return new DefaultSession(
						NotesFactory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]));
			} catch (NotesException e) {
				throw new RiverException(e);
			}
		}

		throw new RiverException(
				"Valid parameters: (A) one lotus.domino.Session or (B) three Strings in this order: server, username and password.");
	}

	static Database createDatabase(org.riverframework.wrapper.Session session, lotus.domino.Database __database) {
		Database _database = new DefaultDatabase(session, __database);
		pool.getObject(_database);
		return _database;
	}
	
	static Document createDocument(org.riverframework.wrapper.Session session, lotus.domino.Document __doc) {
		Document _doc = new DefaultDocument(session, __doc);
		pool.getObject(_doc);
		return _doc;
	}
	
	static View createView(org.riverframework.wrapper.Session session, lotus.domino.View __view) {
		View _view = new DefaultView(session, __view);
		pool.getObject(_view);
		return _view;
	}
	
	static void close() {
		pool.close();
	}
}
