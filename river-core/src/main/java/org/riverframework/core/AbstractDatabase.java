package org.riverframework.core;

import java.lang.reflect.Constructor;

import org.riverframework.ClosedObjectException;
import org.riverframework.RiverException;

/**
 * It is used to manage Databases by default, if we don't need to create a class for each database accessed.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractDatabase<T extends AbstractDatabase<T>> implements Database {
	protected Session session = null;
	protected org.riverframework.wrapper.Database<?> _database = null;

	protected AbstractDatabase(Session session, org.riverframework.wrapper.Database<?> _database) {
		this.session = session;
		this._database = _database;
	}

	protected abstract T getThis();

	@Override
	public String getObjectId() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getObjectId();
	}

	@Override
	public org.riverframework.wrapper.Database<?> getWrapperObject() {
		return _database;
	}

	@Override
	public Object getNativeObject() {
		return _database.getNativeObject();
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public String getServer() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getServer();
	}

	@Override
	public String getFilePath() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getFilePath();
	}

	@Override
	public String getName() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");
		return _database.getName();
	}

	@Override
	public boolean isOpen() {
		return (_database != null && _database.isOpen());
	}

	@Override
	public Document createDocument(String... parameters) {
		org.riverframework.wrapper.Document<?> _doc = _database.createDocument();
		AbstractDocument<?> doc = new DefaultDocument(this, _doc);

		doc.afterCreate();
		doc.isModified = false;

		return doc;
	}

	@Override
	public Document getDocument(String... parameters) {
		return getDocument(false, parameters);
	}

	@Override
	public Document getDocument(boolean createIfDoesNotExist, String... parameters) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

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
	public Document getDocument(org.riverframework.wrapper.Document<?> _doc) {
		Document doc = new DefaultDocument(this, _doc);
		return doc;
	}

	@Override
	public Document getClosedDocument() {
		Document doc = new DefaultDocument(this, null);
		return doc;
	}

	@Override
	public <U extends AbstractDocument<?>> U getClosedDocument(Class<U> clazz) {
		U doc = null;

		try {
			Constructor<?> constructor =
					clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
			constructor.setAccessible(true);
			doc = clazz.cast(constructor.newInstance(this, null));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return doc;
	}

	@Override
	public <U extends AbstractDocument<?>> U createDocument(Class<U> clazz, String... parameters) {
		org.riverframework.wrapper.Document<?> _doc = _database.createDocument(parameters);
		U doc = null;

		try {
			Constructor<?> constructor =
					clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
			constructor.setAccessible(true);
			doc = clazz.cast(constructor.newInstance(this, _doc));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		doc.afterCreate();
		doc.isModified = false;

		return doc;
	}

	@Override
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz, String... parameters) {
		return getDocument(clazz, false, parameters);
	}

	@Override
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz, org.riverframework.wrapper.Document<?> _doc) {
		if (!isOpen())
			throw new ClosedObjectException("The Session object is closed.");

		U doc = null;

		try {
			Constructor<?> constructor =
					clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.Document.class);
			constructor.setAccessible(true);
			doc = clazz.cast(constructor.newInstance(this, _doc));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return doc;
	}

	@Override
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz, boolean createIfDoesNotExist,
			String... parameters) {
		if (!isOpen())
			throw new ClosedObjectException("The Session object is closed.");
		org.riverframework.wrapper.Document<?> _doc = _database.getDocument(parameters);
		U doc = null;

		if ((_doc == null || !_doc.isOpen()) && createIfDoesNotExist) {
			// Creating a new document
			doc = createDocument(clazz, parameters);
		} else {
			// Returning what was found
			doc = getDocument(clazz, _doc);
		}

		return doc;
	}

	@Override
	public View createView(String... parameters) {
		return createView(DefaultView.class, parameters);
	}

	@Override
	public <U extends AbstractView<?>> U createView(Class<U> clazz, String... parameters) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.View<?> _view = _database.createView(parameters);
		U view = null;

		try {
			Constructor<?> constructor =
					clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.View.class);
			constructor.setAccessible(true);
			view = clazz.cast(constructor.newInstance(this, _view));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return view;
	}

	@Override
	public View getView(String... parameters) {
		return getView(DefaultView.class, parameters);
	}

	@Override
	public <U extends AbstractView<?>> U getView(Class<U> clazz, String... parameters) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		String id = parameters[0];
		org.riverframework.wrapper.View<?> _view = _database.getView(id);
		U view = null;

		try {
			Constructor<?> constructor =
					clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.View.class);
			constructor.setAccessible(true);
			view = clazz.cast(constructor.newInstance(this, _view));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return view;
	}

	@Override
	public View getClosedView() {
		return getClosedView(DefaultView.class);
	}

	@Override
	public <U extends AbstractView<?>> U getClosedView(Class<U> clazz) {
		U view = null;

		try {
			Constructor<?> constructor =
					clazz.getDeclaredConstructor(Database.class, org.riverframework.wrapper.View.class);
			constructor.setAccessible(true);
			view = clazz.cast(constructor.newInstance(this, null));
		} catch (Exception e) {
			throw new RiverException(e);
		}

		return view;
	}

	@Override
	public DocumentIterator getAllDocuments() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.DocumentIterator<?, ?> _iterator = _database.getAllDocuments();
		DocumentIterator result = new DefaultDocumentIterator(this, _iterator);

		return result;
	}

	@Override
	public void delete() {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		_database.delete();
	}

	@Override
	public DocumentIterator search(String query) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		org.riverframework.wrapper.DocumentIterator<?, ?> _iterator = _database.search(query);
		DocumentIterator result = new DefaultDocumentIterator(this, _iterator);

		return result;
	}

	@Override
	public T refreshSearchIndex(boolean createIfDoesNotExist) {
		if (!isOpen())
			throw new ClosedObjectException("The Database object is closed.");

		_database.refreshSearchIndex(createIfDoesNotExist);
		return getThis();
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
