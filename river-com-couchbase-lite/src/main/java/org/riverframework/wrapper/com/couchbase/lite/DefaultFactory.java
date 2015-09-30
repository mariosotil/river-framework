package org.riverframework.wrapper.com.couchbase.lite;

import java.io.IOException;

import org.riverframework.RiverException;
import org.riverframework.wrapper.AbstractNativeReference;
import org.riverframework.wrapper.Base;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

import com.couchbase.lite.JavaContext;
import com.couchbase.lite.Manager;

public class DefaultFactory extends org.riverframework.wrapper.AbstractFactory<Object> {

	private static DefaultFactory instance = null;

	protected DefaultFactory(Class<? extends AbstractNativeReference<Object>> nativeReferenceClass) {
		super(nativeReferenceClass);
	}

	public static DefaultFactory getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			if (instance == null) {
				instance = new DefaultFactory(DefaultNativeReference.class);
			}
		}
		return instance;
	}

	@Override
	protected boolean isValidNativeObject(Object __native) {
		return __native != null;
	}

	@SuppressWarnings("unchecked")
	public Session<com.couchbase.lite.Manager> getSession(Object... parameters) {
		com.couchbase.lite.Manager __obj = null;

		if (parameters.length == 1 && parameters[0] instanceof com.couchbase.lite.Manager) {
			log.finer("Creating a session with one com.couchbase.lite.Manager parameter");

			__obj = (com.couchbase.lite.Manager) parameters[0];
		}
		else {
			log.finer("Creating a default session");
			try {
				__obj = new Manager(new JavaContext(), Manager.DEFAULT_OPTIONS);
			} catch (IOException e) {
				throw new RiverException(e);
			}
		}	
		_session = getWrapper(DefaultSession.class, com.couchbase.lite.Manager.class, __obj); 
		return (Session<com.couchbase.lite.Manager>) _session; 
	}

	@Override
	public <U extends Object> Database<com.couchbase.lite.Database> getDatabase(U __obj) {
		return getWrapper(DefaultDatabase.class, com.couchbase.lite.Database.class, __obj);
	}

	@Override
	public <U extends Object> Document<com.couchbase.lite.Document> getDocument(U __obj) {
		return getWrapper(DefaultDocument.class, com.couchbase.lite.Document.class, __obj);
	}

	@Override
	public Document<com.couchbase.lite.Document> getDocument(String objectId) {
		return getWrapper(DefaultDocument.class, com.couchbase.lite.Document.class, objectId);
	}

	@Override
	public <U extends Object> View<com.couchbase.lite.View> getView(U __obj) {
		return getWrapper(DefaultView.class, com.couchbase.lite.View.class, __obj);
	}

	@Override
	public <U extends Object> DocumentIterator<Object,com.couchbase.lite.Document> getDocumentIterator(U __obj) {
		DocumentIterator<Object,com.couchbase.lite.Document> _iterator = null;

		//_iterator = getWrapper(DefaultDocumentIterator.class, org.openntf.domino.DocumentCollection.class, __obj);

		return _iterator;
	}

	@Override
	public void cleanUp(@SuppressWarnings("unchecked") Base<? extends Object>... except) {

	}

	@Override
	public boolean getIsRemoteSession() {
		return false;
	}
}