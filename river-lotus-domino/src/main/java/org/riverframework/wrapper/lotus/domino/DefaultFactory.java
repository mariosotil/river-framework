package org.riverframework.wrapper.lotus.domino;

import java.lang.ref.Reference;
import java.util.logging.Level;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

import org.riverframework.RiverException;
import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

public class DefaultFactory extends org.riverframework.wrapper.AbstractFactory<lotus.domino.Base> {

	private static DefaultFactory instance = null;
	private boolean isRemoteSession = false;

	protected DefaultFactory(Class<? extends AbstractNativeReference<lotus.domino.Base>> nativeReferenceClass) {
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
	public boolean getIsRemoteSession() {
		return isRemoteSession;
	} 

	@Override
	protected boolean isValidNativeObject(lotus.domino.Base __native) {
		return __native != null; // && !DefaultBase.isRecycled(__native);
	}

	@SuppressWarnings("unchecked")
	public Session<lotus.domino.Session> getSession(Object... parameters) {
		lotus.domino.Session __obj = null;

		if (parameters.length == 1 && parameters[0] instanceof lotus.domino.Session) {
			log.finer("Creating a session with one lotus.domino.Session parameter");

			__obj = (lotus.domino.Session) parameters[0];

			_session = getWrapper(DefaultSession.class, lotus.domino.Session.class, __obj); 
			return (Session<lotus.domino.Session>) _session; 
		}

		if (parameters.length == 3 && parameters[2] instanceof String) {
			log.finer("Creating a session with three parameters: server, username, password");

			try {
				isRemoteSession = (parameters[0] != null);					

				__obj = NotesFactory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]); 
			} catch (NotesException e) {
				throw new RiverException(e);
			}

			_session = getWrapper(DefaultSession.class, lotus.domino.Session.class, __obj);
			return (Session<lotus.domino.Session>) _session; 
		}

		throw new RiverException(
				"Valid parameters: (A) one lotus.domino.Session or (B) three Strings in this order: server, username and password.");
	}

	@Override
	public <U extends lotus.domino.Base> Database<lotus.domino.Database> getDatabase(U __obj) {
		return getWrapper(DefaultDatabase.class, lotus.domino.Database.class, __obj);
	}

	@Override
	public Document<lotus.domino.Document> getDocument(String objectId) {
		return getWrapper(DefaultDocument.class, lotus.domino.Document.class, objectId);
	}

	@Override
	public <U extends lotus.domino.Base> Document<lotus.domino.Document> getDocument(U __obj) {
		return getWrapper(DefaultDocument.class, lotus.domino.Document.class, __obj);
	}

	@Override
	public <U extends lotus.domino.Base> View<lotus.domino.View> getView(U __obj) {
		return getWrapper(DefaultView.class, lotus.domino.View.class, __obj);
	}

	@Override
	public <U extends lotus.domino.Base> DocumentIterator<lotus.domino.Base, lotus.domino.Document> getDocumentIterator(U __obj) {
		DocumentIterator<lotus.domino.Base, lotus.domino.Document> _iterator = null;

		if(__obj instanceof lotus.domino.DocumentCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.DocumentCollection.class, __obj);

		} else if(__obj instanceof lotus.domino.ViewEntryCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.ViewEntryCollection.class, __obj);

		} else if(__obj instanceof lotus.domino.View) {
			lotus.domino.ViewEntryCollection __all;
			try {
				__all = ((lotus.domino.View) __obj).getAllEntries();
			} catch (NotesException e) {
				throw new RiverException(e);
			}
			_iterator = getWrapper(DefaultDocumentIterator.class, lotus.domino.ViewEntryCollection.class, __all);

		} else {
			throw new RiverException("Expected an object lotus.domino.View, DocumentCollection, or ViewEntryCollection.");
		}

		return _iterator;
	}

	@Override
	public void cleanUp(Base<? extends lotus.domino.Base>... except) {
		Reference<? extends Base<lotus.domino.Base>> ref = null;

		boolean cleaning = false;
		long start = 0;
		String exceptId = null;

		// If there is no a session created, return
		if (_session == null) return;


		if (except.length > 0) {
			// If the object to be keeped is not a document, return
			if (!(except[0] instanceof org.riverframework.wrapper.Document)) return;

			// Otherwise, we save its Object Id
			exceptId = except[0].getObjectId();
		} else {
			exceptId = "";
		}

		while ((ref = wrapperQueue.poll()) != null) { 
			if (!cleaning) {
				start = System.nanoTime();
				cleaning = true;
			}

			// synchronized (_session){  <== necessary?							
			AbstractNativeReference<lotus.domino.Base> nat = nativeReferenceClass.cast(ref);
			lotus.domino.Base __native = nat.getNativeObject();

			if(__native instanceof lotus.domino.local.Document || 
					__native instanceof lotus.domino.cso.Document) {				
				String id = nat.getObjectId();

				if(exceptId.equals(id)) {
					if (log.isLoggable(Level.FINEST)) {
						StringBuilder sb = new StringBuilder();
						sb.append("NO Recycling: id=");
						sb.append(id);
						sb.append(" native=");
						sb.append(__native == null ? "<null>" : __native.getClass().getName());
						sb.append(" (");
						sb.append(__native == null ? "<null>" : __native.hashCode());
						sb.append(")");

						log.finest(sb.toString());
					}

				} else {
					if (log.isLoggable(Level.FINEST)) {
						StringBuilder sb = new StringBuilder();
						sb.append("Recycling: id=");
						sb.append(id);
						sb.append(" native=");
						sb.append(__native == null ? "<null>" : __native.getClass().getName());
						sb.append(" (");
						sb.append(__native == null ? "<null>" : __native.hashCode());
						sb.append(")");

						log.finest(sb.toString());
					}

					wrapperMap.remove(id);
					nat.close();
				}
			}
			// }
		}

		if (cleaning) {
			long end = System.nanoTime(); 
			log.finest("Finished clean up. Elapsed time=" + ((end - start)/1000000) + "ms");
			cleaning = false;
		}
	}


}