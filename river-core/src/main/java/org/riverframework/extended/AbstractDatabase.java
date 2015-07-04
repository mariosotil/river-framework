package org.riverframework.extended;

import org.riverframework.core.Database;
import org.riverframework.core.DefaultCounter;
import org.riverframework.core.Document;
import org.riverframework.core.DocumentIterator;
import org.riverframework.core.Session;
import org.riverframework.core.View;

public abstract class AbstractDatabase<T extends AbstractDatabase<T>> implements Base, Database {
	protected Database database = null;

	public AbstractDatabase(Database database) {
		this.database = database;
	}

	@Override
	public boolean isOpen() {
		return (database != null && database.isOpen());
	}

	@Override
	public String getObjectId() {
		return database.getObjectId();
	}

	public org.riverframework.core.Database getDatabase() {
		return database;
	}

	@Override
	public Document createDocument(String... parameters) {
		Document doc = database.createDocument(parameters);

		return doc;
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDocument<?>> U createDocument(Class<U> clazz, String... parameters) {
		U xDoc = database.createDocument(clazz, parameters);

		return xDoc;
	}

	@Override
	public Document getDocument(String... parameters) {
		Document doc = database.getDocument(parameters);

		return doc;
	}

	@Override
	public <U extends org.riverframework.extended.AbstractDocument<?>> U getDocument(Class<U> clazz, String... parameters) {
		U xDoc = database.getDocument(clazz, parameters);

		return xDoc;
	}

	public T deleteAll() {
		database.getAllDocuments().deleteAll();

		return getThis();
	}

	protected abstract T getThis();

	@Override
	public void close() {
		database.close();
	}

	public org.riverframework.extended.Counter getCounter(String key) {
		Counter counter = database.getDocument(DefaultCounter.class, key);

		return counter;
	}

	@Override
	public Session getSession() {
		return database.getSession();
	}

	@Override
	public org.riverframework.wrapper.Database<?> getWrapperObject() {
		return database.getWrapperObject();
	}

	@Override
	public View createView(String... parameters) {
		return database.createView(parameters);
	}

	@Override
	public View getView(String... parameters) {
		return database.getView(parameters);
	}

	@Override
	public Document getDocument(boolean createIfDoesNotExist, String... parameters) {
		return database.getDocument(createIfDoesNotExist, parameters);
	}

	@Override
	public Document getDocument(org.riverframework.wrapper.Document<?> doc) {
		return database.getDocument(doc);
	}

	@Override
	public <U extends AbstractDocument<?>> U getDocument(Class<U> clazz, boolean createIfDoesNotExist, String... parameters) {
		return database.getDocument(clazz, createIfDoesNotExist, parameters);
	}

	@Override
	public DocumentIterator getAllDocuments() {
		return database.getAllDocuments();
	}

	@Override
	public void delete() {
		database.delete();
	}

	@Override
	public DocumentIterator search(String query) {
		return database.search(query);
	}

	@Override
	public Database refreshSearchIndex(boolean createIfNotExist) {
		return database.refreshSearchIndex(createIfNotExist);
	}

	@Override
	public String getServer() {
		return database.getServer();
	}

	@Override
	public String getFilePath() {
		return database.getFilePath();
	}

	@Override
	public String getName() {
		return database.getName();
	}
}
