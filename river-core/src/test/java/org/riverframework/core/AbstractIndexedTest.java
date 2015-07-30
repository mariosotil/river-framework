package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;
import org.riverframework.wrapper.Document;

public abstract class AbstractIndexedTest {
	protected Session session = null;
	protected Context context = null;
	protected IndexedDatabase database = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				String className = this.getClass().getPackage().getName()
						+ ".Context";
				Class<?> clazz = Class.forName(className);
				if (org.riverframework.Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = context.getSession();
				database = session.getDatabase(UniqueDatabase.class,
						context.getTestDatabaseServer(),
						context.getTestDatabasePath());
				database.getAllDocuments().deleteAll();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void close() {
		context.closeSession();
	}

	static class UniqueDatabase extends AbstractIndexedDatabase<UniqueDatabase> {

		protected UniqueDatabase(Session session,
				org.riverframework.wrapper.Database<?> _database) {
			super(session, _database);

			registerDocumentClass(UniqueDocument.class);
			registerDocumentClass(NoUniqueDocument.class); // It should be
															// ignored
		}

		@Override
		protected UniqueDatabase getThis() {
			return this;
		}
	}

	static class NoUniqueDocument extends AbstractDocument<NoUniqueDocument> {

		protected NoUniqueDocument(Database database, Document<?> _doc) {
			super(database, _doc);
		}

		protected final static String FORM_NAME = Session.ELEMENT_PREFIX
				+ "NoUnique";
		protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";

		@Override
		public NoUniqueDocument afterCreate() {
			setField("Form", FORM_NAME);
			return this;
		}

		@Override
		protected NoUniqueDocument getThis() {
			return this;
		}
	}

	static class UniqueDocument extends AbstractIndexedDocument<UniqueDocument> {
		protected final static String FORM_NAME = Session.ELEMENT_PREFIX
				+ "Unique";
		protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";

		protected static View index = null;

		protected UniqueDocument(Database database, Document<?> _doc) {
			super(database, _doc);
		}

		@Override
		public String getTableName() {
			return FORM_NAME;
		}

		@Override
		protected String[] getParametersToCreateIndex() {
			// TODO: this works only for IBM Notes. FIX IT!
			return new String[] { getIndexName(),
					"Form=\"" + getTableName() + "\"" };
		}

		@Override
		public String getIdField() {
			return FIELD_ID;
		}

		@Override
		public String getIndexName() {
			return Session.ELEMENT_PREFIX + "Unique_Index";
		}

		@Override
		public UniqueDocument generateId() {
			// Do nothing
			return this;
		}

		@Override
		protected UniqueDocument getThis() {
			return this;
		}

	}

	UniqueDatabase indexedDatabase = null;

	@Test
	public void testId() {
		assertTrue("The test database could not be opened.", database.isOpen());

		UniqueDocument unique = database.createDocument(UniqueDocument.class);
		assertTrue("The document must be open.", unique.isOpen());

		RandomString rs = new RandomString(10);

		String key = rs.nextString();
		unique.setId(key).save();

		String oldKey = unique.getId();
		assertTrue("The Id retrieved is different to the saved.",
				key.equals(oldKey));

		database.getIndex(UniqueDocument.class).refresh();

		unique = null;
		unique = database.getDocument(UniqueDocument.class, key);
		assertTrue(
				"A document that was created, could not be opened with the key '"
						+ key + "'.", unique.isOpen());

		unique = null;
		unique = database.getDocument(UniqueDocument.class,
				"%%%THIS DOCUMENT DOES NOT EXIST%%%");
		assertFalse("A document that should not be found, is opened.",
				unique.isOpen());

		unique = null;
		unique = database.getDocument(UniqueDocument.class, true, "NEW_KEY_"
				+ key);
		assertTrue("A document that should be created, is not opened.",
				unique.isOpen());

		unique.save(true);

		oldKey = unique.getId();
		assertTrue(
				"A document created by 'createIfDoesNotExist', has a wrong Id.",
				oldKey.equals("NEW_KEY_" + key));

	}
}
