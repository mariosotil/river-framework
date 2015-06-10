package org.riverframework.wrapper.lotus.domino;

import java.util.LinkedList;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

import java.lang.ref.Reference;

public class DefaultFactory extends org.riverframework.wrapper.AbstractFactory<lotus.domino.Base> {
	private static DefaultFactory instance = null;
	protected volatile LinkedList<Reference<? extends Base>> list = null;
	private NativeReferenceCollector rc = null;

	protected DefaultFactory(org.riverframework.wrapper.Session _session) {
		super(_session);
		
		list = new LinkedList<Reference<? extends Base>>();
		rc = new NativeReferenceCollector(queue, list);
		rc.start();
	}

	public static DefaultFactory getInstance(org.riverframework.wrapper.Session _session) {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (DefaultFactory.class) {
				if (instance == null) {
					instance = new DefaultFactory(_session);
				}
			}
		}
		return instance;
	}

	protected void addToCache(String id, Base _wrapper) {
		super.addToCache(id, _wrapper);
		list.add(new NativeReference(_wrapper, queue));
	}

	@SuppressWarnings("unused")
	private static Session getSession(Object... parameters) {
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

	@Override
	public Database getDatabase(lotus.domino.Base __obj) {
		return getWrapper(DefaultDatabase.class, lotus.domino.Database.class, __obj);
	}

	@Override
	public Document getDocument(lotus.domino.Base __obj) {
		if(__obj != null && !(__obj instanceof lotus.domino.Document)) 
			throw new RiverException("Expected an object lotus.domino.Document");
				
		Document _doc = null;
		
		try {
			if (__obj != null && ((lotus.domino.Document) __obj).isDeleted()) {
				__obj.recycle();
				__obj = null;
			} 
		} catch (Exception e) {
			throw new RiverException("There was a problem creating the document wrapper.");
		}

		_doc = getWrapper(DefaultDocument.class, lotus.domino.Document.class, __obj);

		return _doc;
	}

	@Override
	public View getView(lotus.domino.Base __obj) {
		return getWrapper(DefaultView.class, lotus.domino.View.class, __obj);
	}

	@Override
	public DocumentIterator getDocumentIterator(lotus.domino.Base __obj) {
		DocumentIterator _iterator = null;

		if(__obj instanceof lotus.domino.DocumentCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.DocumentCollection.class, __obj);

		} else if(__obj instanceof lotus.domino.ViewEntryCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.ViewEntryCollection.class, __obj);

		} else if(__obj instanceof lotus.domino.View) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.View.class, __obj);

		} else {
			throw new RiverException("Expected an object lotus.domino.View, DocumentCollection, or ViewEntryCollection.");
		}

		return _iterator;
	}

	@Override
	public void close() {
		rc.terminate();
		log.fine("Cleaning the last objects in the phantom reference list: " + list.size());
		list.clear();

		super.close();
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}