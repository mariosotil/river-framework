package local.nosql;

import local.mock.DatabaseException;
import local.mock.Factory;
import org.riverframework.RiverException;
import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.AbstractFactory;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.View;
import java.lang.ref.Reference;
import java.util.logging.Level;

public class DefaultFactory extends AbstractFactory<local.mock.Base> {

	private static DefaultFactory instance = null;
	private boolean isRemoteSession = false;

	protected DefaultFactory(Class<? extends AbstractNativeReference<local.mock.Base>> nativeReferenceClass) {
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
	protected boolean isValidNativeObject(local.mock.Base __native) {
		return __native != null; // && !DefaultBase.isRecycled(__native);
	}

	@SuppressWarnings("unchecked")
	public Session<local.mock.Session> getSession(Object... parameters) {
		local.mock.Session __obj = null;

		if (parameters.length == 1 && parameters[0] instanceof local.mock.Session) {
			log.finer("Creating a session with one org.riverframework.mock.Session parameter");

			__obj = (local.mock.Session) parameters[0];

			_session = getWrapper(DefaultSession.class, local.mock.Session.class, __obj);
			return (Session<local.mock.Session>) _session;
		}

		if (parameters.length == 3 && parameters[2] instanceof String) {
			log.finer("Creating a session with three parameters: server, username, password");

			try {
				isRemoteSession = (parameters[0] != null);					

				__obj = Factory.createSession((String) parameters[0], (String) parameters[1], (String) parameters[2]);
			} catch (DatabaseException e) {
				throw new RiverException(e);
			}

			_session = getWrapper(DefaultSession.class, local.mock.Session.class, __obj);
			return (Session<local.mock.Session>) _session;
		}

		throw new RiverException(
				"Valid parameters: (A) one org.riverframework.mock.Session or (B) three Strings in this order: server, username and password.");
	}

	@Override
	public <U extends local.mock.Base> Database<local.mock.Database> getDatabase(U __obj) {
		return getWrapper(DefaultDatabase.class, local.mock.Database.class, __obj);
	}

	@Override
	public Document<local.mock.Document> getDocument(String objectId) {
		return getWrapper(DefaultDocument.class, local.mock.Document.class, objectId);
	}

	@Override
	public <U extends local.mock.Base> Document<local.mock.Document> getDocument(U __obj) {
		return getWrapper(DefaultDocument.class, local.mock.Document.class, __obj);
	}

	@Override
	public <U extends local.mock.Base> View<local.mock.View> getView(U __obj) {
		return getWrapper(DefaultView.class, local.mock.View.class, __obj);
	}

	@Override
	public <U extends local.mock.Base> DocumentIterator<local.mock.Base, local.mock.Document> getDocumentIterator(U __obj) {
		DocumentIterator<local.mock.Base, local.mock.Document> _iterator = null;

		if(__obj instanceof local.mock.DocumentCollection) {
			_iterator = getWrapper(DefaultDocumentIterator.class, local.mock.DocumentCollection.class, __obj);

		} else {
			throw new RiverException("Expected an object org.riverframework.mock.View, DocumentCollection, or ViewEntryCollection.");
		}

		return _iterator;
	}

	@Override
	public void cleanUp(Base<? extends local.mock.Base>... except) {
		Reference<? extends Base<local.mock.Base>> ref = null;

		boolean cleaning = false;
		long start = 0;
		String exceptId = null;

		// If there is no a session created, return
		if (_session == null) return;


		if (except.length > 0) {
			// If the object to be keeped is not a document, return
			if (!(except[0] instanceof local.mock.Document)) return;

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
			AbstractNativeReference<local.mock.Base> nat = nativeReferenceClass.cast(ref);
			local.mock.Base __native = nat.getNativeObject();

			if(__native instanceof local.mock.Document ||
					__native instanceof local.mock.Document) {
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