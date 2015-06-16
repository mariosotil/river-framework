package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.Reference;
import java.util.logging.Level;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

public class DefaultFactory extends org.riverframework.wrapper.AbstractFactory<org.openntf.domino.Base> {

	private static DefaultFactory instance = null;

	protected DefaultFactory(Class<? extends AbstractNativeReference<org.openntf.domino.Base>> nativeReferenceClass) {
		super(nativeReferenceClass);
	}

	public static DefaultFactory getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (DefaultFactory.class) {
				if (instance == null) {
					instance = new DefaultFactory(DefaultNativeReference.class);
				}
			}
		}
		return instance;
	}

	@Override
	protected boolean isValidNativeObject(org.openntf.domino.Base __native) {
		return __native != null;
	}

	public Session<org.openntf.domino.Base> getSession(Object... parameters) {
		org.openntf.domino.Session __obj = null;

		if (parameters.length == 1 && parameters[0] instanceof org.openntf.domino.Session) {
			log.finer("Creating a session with one org.openntf.domino.Session parameter");

			__obj = (org.openntf.domino.Session) parameters[0];

			_session = getWrapper(DefaultSession.class, org.openntf.domino.Session.class, __obj); 
			return _session; 
		}

		if (parameters.length == 3 && parameters[2] instanceof String) {
			log.finer("Creating a session with three parameters: server, username, password");

			try {
				__obj = (org.openntf.domino.Session) org.openntf.domino.utils.Factory
						.fromLotus(NotesFactory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]),
								org.openntf.domino.Session.class, null);
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			_session = getWrapper(DefaultSession.class, org.openntf.domino.Session.class, __obj);
			return _session; 
		}

		throw new RiverException(
				"Valid parameters: (A) one org.openntf.domino.Session or (B) three Strings in this order: server, username and password.");
	}

	@Override
	public Database<org.openntf.domino.Base> getDatabase(org.openntf.domino.Base __obj) {
		return getWrapper(DefaultDatabase.class, org.openntf.domino.Database.class, __obj);
	}

	@Override
	public Document<org.openntf.domino.Base> getDocument(org.openntf.domino.Base __obj) {
		return getWrapper(DefaultDocument.class, org.openntf.domino.Document.class, __obj);
	}

	@Override
	public View<org.openntf.domino.Base> getView(org.openntf.domino.Base __obj) {
		return getWrapper(DefaultView.class, org.openntf.domino.View.class, __obj);
	}

	@Override
	public DocumentIterator<org.openntf.domino.Base> getDocumentIterator(org.openntf.domino.Base __obj) {
		DocumentIterator<org.openntf.domino.Base> _iterator = null;

		if(__obj instanceof org.openntf.domino.DocumentCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, org.openntf.domino.DocumentCollection.class, __obj);

		} else if(__obj instanceof org.openntf.domino.ViewEntryCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, org.openntf.domino.ViewEntryCollection.class, __obj);

		} else if(__obj instanceof org.openntf.domino.View) {
			_iterator = getWrapper(DefaultDocumentIterator.class, org.openntf.domino.View.class, __obj);

		} else {
			throw new RiverException("Expected an object org.openntf.domino.View, DocumentCollection, or ViewEntryCollection.");
		}

		return _iterator;
	}

	@Override
	public void cleanUp() {

	}
}