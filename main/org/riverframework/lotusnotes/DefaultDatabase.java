package org.riverframework.lotusnotes;

import java.lang.reflect.Constructor;

import org.riverframework.RiverException;

/*
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public class DefaultDatabase extends org.riverframework.fw.AbstractDatabase<lotus.domino.Database> {

	public DefaultDatabase() {
		super();
	}

	public DefaultDatabase(lotus.domino.Database obj) {
		super(obj);
	}

	public DefaultDatabase(String... location) {
		super(location);
	}

	@Override
	public org.riverframework.lotusnotes.DefaultDatabase open(String... location) {
		String server = location[0];
		String path = location[1];

		try {
			database = DefaultSession.getInstance().getNotesSession()
					.getDatabase(server, path, false);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return this;
	}

	@Override
	public boolean isOpen() {
		try {
			return super.isOpen() && database.isOpen();
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	public <U extends org.riverframework.Document<?>> U createDocument(Class<U> type, String... parameters) {
		U rDoc = null;
		lotus.domino.Document doc = null;

		try {
			if (type != null && DefaultDocument.class.isAssignableFrom(type)) {

				doc = database.createDocument();
				doc.replaceItemValue(org.riverframework.Document.FIELD_CLASS, type.getSimpleName());

				Constructor<?> constructor = type.getConstructor(DefaultDatabase.class, lotus.domino.Document.class);
				rDoc = type.cast(constructor.newInstance(this, doc));
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		if (rDoc != null) {
			((DefaultDocument) rDoc).afterCreate();
		}

		return rDoc;
	}

	@Override
	public <U extends org.riverframework.Document<?>> U getDocument(Class<U> type, String... parameters)
	{
		U rDoc = null;
		lotus.domino.Document doc = null;
		String id = parameters[0];

		try {
			if (id.length() == 32) {
				doc = database.getDocumentByUNID(id);
			}

			if (doc == null && (id).length() == 8) {
				doc = database.getDocumentByID(id);
			}

			if (type != null && DefaultDocument.class.isAssignableFrom(type)) {
				Constructor<?> constructor = type.getConstructor(DefaultDatabase.class, lotus.domino.Document.class);
				rDoc = type.cast(constructor.newInstance(this, doc));
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rDoc;
	}

	@Override
	public <U extends org.riverframework.View<?>> U getView(Class<U> type, String... parameters) {
		U rView = null;
		lotus.domino.View view = null;
		String id = parameters[0];

		try {
			view = database.getView(id);

			if (type != null && DefaultView.class.isAssignableFrom(type)) {
				Constructor<?> constructor = type.getConstructor(DefaultDatabase.class, lotus.domino.View.class);
				rView = type.cast(constructor.newInstance(this, view));
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return rView;
	}

	@Override
	public org.riverframework.lotusnotes.DefaultDocumentCollection getAllDocuments() {
		org.riverframework.lotusnotes.DefaultDocumentCollection rDocumentIterator = null;
		lotus.domino.DocumentCollection col = null;

		try {
			col = database.getAllDocuments();
		} catch (Exception e) {
			throw new RiverException(e);
		}

		rDocumentIterator = new org.riverframework.lotusnotes.DefaultDocumentCollection(this, col);

		return rDocumentIterator;
	}

	@Override
	protected void close() {
		try {
			if (database != null) {
				database.recycle();
				database = null;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}
}
