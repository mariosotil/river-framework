package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.NotesThread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Database;
import org.riverframework.RandomString;
import org.riverframework.Session;
import org.riverframework.Unique;
import org.riverframework.core.DefaultDatabase;
import org.riverframework.core.DefaultDocument;
import org.riverframework.core.DefaultSession;

public class UniqueTest {
	private Session session = DefaultSession.getInstance();
	private Context context = Context.getInstance();
	private org.riverframework.Database database = null;

	@Before
	public void init() {
		NotesThread.sinitThread();

		session.open(context.getSession());
		database = session.getDatabase(DefaultDatabase.class, "", context.getDatabase());
		database.getAllDocuments().deleteAll();
	}

	@After
	public void close() {
		session.close();
		NotesThread.stermThread();
	}

	static class NoUniqueDocument extends DefaultDocument {
		protected final static String FORM_NAME = Session.OBJECT_PREFIX + "NoUnique";
		protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";

		protected NoUniqueDocument(Database d, org.riverframework.wrapper.Document doc) {
			super(d, doc);
		}

		@Override
		protected NoUniqueDocument afterCreate() {
			setForm(FORM_NAME);
			return this;
		}
	}

	static class UniqueDocument extends DefaultDocument implements Unique {
		protected final static String FORM_NAME = Session.OBJECT_PREFIX + "Unique";
		protected final static String FIELD_ID = Session.FIELD_PREFIX + "id";

		protected UniqueDocument(Database d, org.riverframework.wrapper.Document doc) {
			super(d, doc);
		}

		public static String getIndexName() {
			return Session.OBJECT_PREFIX + "Unique_Index";
		}

		@Override
		protected UniqueDocument afterCreate() {
			setForm(FORM_NAME);
			return this;
		}

		@Override
		public String getId() {
			String id = getFieldAsString(FIELD_ID);
			return id;
		}

		@Override
		public org.riverframework.Document setId(String id) {
			setField(FIELD_ID, id);
			return this;
		}

		@Override
		public org.riverframework.Document generateId() {
			// Do nothing
			return this;
		}
	}

	@Test
	public void testId() {
		assertTrue("The test database could not be opened.", database.isOpen());

		UniqueDocument unique = database.createDocument(UniqueDocument.class);
		assertTrue("The document must be open.", unique.isOpen());

		RandomString rs = new RandomString(10);

		String key = rs.nextString();
		unique.setId(key).save();

		String oldKey = unique.getId();
		assertTrue("The Id retrieved is different to the saved.", key.equals(oldKey));

		unique = null;
		unique = database.getDocument(UniqueDocument.class, key);
		assertTrue("A document that was created, could not be opened with the key '" + key + "'.", unique.isOpen());

		unique = null;
		unique = database.getDocument(UniqueDocument.class, "%%%THIS DOCUMENT DOES NOT EXIST%%%");
		assertFalse("A document that should not be found, is opened.", unique.isOpen());

		unique = null;
		unique = database.getDocument(UniqueDocument.class, true, "NEW_KEY_" + key);
		assertTrue("A document that should be created, is not opened.", unique.isOpen());

		oldKey = unique.getId();
		assertTrue("A document created by 'createIfDoesNotExist', has a wrong Id.", oldKey.equals("NEW_KEY_" + key));

	}
}
