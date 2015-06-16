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

public class DefaultFactory extends org.riverframework.wrapper.AbstractFactory<lotus.domino.Base> {

	private static DefaultFactory instance = null;

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
	protected boolean isValidNativeObject(lotus.domino.Base __native) {
		return __native != null && !DefaultBase.isRecycled(__native);
	}
	
	public Session<lotus.domino.Base> getSession(Object... parameters) {
		lotus.domino.Session __obj = null;
		
		if (parameters.length == 1 && parameters[0] instanceof lotus.domino.Session) {
			log.finer("Creating a session with one lotus.domino.Session parameter");
			
			__obj = (lotus.domino.Session) parameters[0];
			
			_session = getWrapper(DefaultSession.class, lotus.domino.Session.class, __obj); 
			return _session; 
		}

		if (parameters.length == 3 && parameters[2] instanceof String) {
			log.finer("Creating a session with three parameters: server, username, password");
			
			try {
				__obj = NotesFactory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]); 
			} catch (NotesException e) {
				throw new RiverException(e);
			}
			
			_session = getWrapper(DefaultSession.class, lotus.domino.Session.class, __obj);
			return _session; 
		}

		throw new RiverException(
				"Valid parameters: (A) one lotus.domino.Session or (B) three Strings in this order: server, username and password.");
	}

	@Override
	public Database<lotus.domino.Base> getDatabase(lotus.domino.Base __obj) {
		return getWrapper(DefaultDatabase.class, lotus.domino.Database.class, __obj);
	}

	@Override
	public Document<lotus.domino.Base> getDocument(lotus.domino.Base __obj) {
		return getWrapper(DefaultDocument.class, lotus.domino.Document.class, __obj);
	}

	@Override
	public View<lotus.domino.Base> getView(lotus.domino.Base __obj) {
		return getWrapper(DefaultView.class, lotus.domino.View.class, __obj);
	}

	@Override
	public DocumentIterator<lotus.domino.Base> getDocumentIterator(lotus.domino.Base __obj) {
		DocumentIterator<lotus.domino.Base> _iterator = null;

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
	public void cleanUp() {
		// log.finest("Starting clean up");

		Reference<? extends Base<lotus.domino.Base>> ref = null; 
		while ((ref = queue.poll()) != null) {
			synchronized (_session){							
				AbstractNativeReference<lotus.domino.Base> nat = nativeReferenceClass.cast(ref);
				lotus.domino.Base __native = nat.getNativeObject();
				String id = nat.getObjectId();

				if(__native instanceof lotus.domino.local.Document || 
						__native instanceof lotus.domino.cso.Document) {				
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

					try {
						weakWrapperMap.remove(id);
						nat.close();
					} catch (Exception ex) {
						log.log(Level.WARNING, "Exception at recycling", ex);
					}
				}
			}
		}

		// log.finest("Finished clean up");
	}
}