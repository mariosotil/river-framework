package org.riverframework.core;

import java.lang.reflect.Constructor;

import org.riverframework.RiverException;

/**
 * It is used to manage Databases by default, if we don't need to create a class for each database accessed. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultDatabase implements org.riverframework.core.Database {
	protected Session session = null;
	protected org.riverframework.wrapper.Database<?> _database = null;

	protected DefaultDatabase(org.riverframework.core.Session s, org.riverframework.wrapper.Database<?> _db) {
		session = s;
		_database = _db;
	}

	@Override
	public String getObjectId() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");
		return _database.getObjectId();
	}

	@Override
	public org.riverframework.wrapper.Database<?> getWrapperObject() {
		return _database;
	}

	@Override
	public org.riverframework.core.Session getSession() {
		return session;
	}

	@Override
	public String getServer() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");
		return _database.getServer();
	}

	@Override
	public String getFilePath() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");
		return _database.getFilePath();
	}

	@Override
	public String getName() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");
		return _database.getName();
	}

	@Override
	public boolean isOpen() {
		return (_database != null && _database.isOpen());
	}

	@Override
	public org.riverframework.core.Document createDocument(String... parameters) {
		org.riverframework.wrapper.Document<?> _doc = _database.createDocument();
		Document doc = new DefaultDocument(this, _doc);
		
		return doc;
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDocument<?>> U createDocument(Class<U> clazz, String... parameters) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Session object is closed.");

		org.riverframework.core.Document document = createDocument(parameters);
		U xDocument = null;
		
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(org.riverframework.core.Document.class);
			constructor.setAccessible(true);
			xDocument = clazz.cast(constructor.newInstance(document));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return xDocument;
	}

	@Override
	public org.riverframework.core.Document getDocument(String... parameters) {
		return getDocument(false, parameters);
	}

	@Override
	public org.riverframework.core.Document getDocument(boolean createIfDoesNotExist, String... parameters) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.Document<?> _doc = null;
		Document doc = null;
		
		if (parameters.length > 0) {
			String id = parameters[0];

			_doc = _database.getDocument(id);

			if ((_doc == null || !_doc.isOpen()) && createIfDoesNotExist) {
				// Creating a new document
				doc = createDocument(parameters);
			} else {
				// Returning what was found
				doc = getDocument(_doc);
			}
		} else {
			// There's no parameters. Returning a Null object
			doc = getDocument((org.riverframework.wrapper.Document<?>) null);
		}

		return doc;
	}

	@Override
	public org.riverframework.core.Document getDocument(org.riverframework.wrapper.Document<?> _doc) {
		Document doc = new DefaultDocument(this, _doc);
		return doc;
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDocument<?>> U getDocument(Class<U> clazz, String... parameters) {
		return getDocument(clazz, false, parameters);
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDocument<?>> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Session object is closed.");

		org.riverframework.core.Document document = createDocument(parameters);
		U xDocument = null;
		
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(org.riverframework.core.Document.class);
			constructor.setAccessible(true);
			xDocument = clazz.cast(constructor.newInstance(document));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return xDocument;
	}
	
	@Override
	public org.riverframework.core.View createView(String... parameters) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.View<?> _view = _database.createView(parameters);
		View view = new DefaultView(this, _view);

		return view;
	}

	@Override
	public org.riverframework.core.View getView(String... parameters) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");

		String id = parameters[0];
		org.riverframework.wrapper.View<?> _view = _database.getView(id);
		View view = new DefaultView(this, _view);

		return view;
	}

	@Override
	public DocumentIterator getAllDocuments() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.DocumentIterator<?> _iterator = _database.getAllDocuments();
		DocumentIterator result = new DefaultDocumentIterator(this, _iterator);

		return result;
	}

	@Override
	public void delete() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");

		_database.delete();
	}

	@Override
	public DocumentIterator search(String query) {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.DocumentIterator<?> _iterator = _database.search(query);
		DocumentIterator result = new DefaultDocumentIterator(this, _iterator);

		return result;
	}

	@Override
	public org.riverframework.core.Database refreshSearchIndex() {
		// if (!isOpen())
		// throw new ClosedObjectException("The Database object is closed.");

		_database.refreshSearchIndex();
		return this;
	}

	@Override
	public void close() {
		_database.close();
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + getWrapperObject().toString() + ")";
	}
}
