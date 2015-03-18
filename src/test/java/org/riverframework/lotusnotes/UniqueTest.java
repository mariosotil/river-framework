package org.riverframework.lotusnotes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import lotus.domino.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;

public class UniqueTest {
	final Session session = DefaultSession.getInstance();
	private org.riverframework.lotusnotes.Database rDatabase = null;

	@Before
	public void init() {
		try {
			session.open(Context.getServerAndPort(), Context.getUser(), Context.getPassword());
			rDatabase = session.getDatabase(DefaultDatabase.class, Context.getServer(), Context.getDatabase());

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@After
	public void close() {
		session.close();
	}

	static class NoUniqueDocument extends DefaultDocument {
		protected final static String FORM_NAME = Session.PREFIX + "NoUnique";
		protected final static String FIELD_ID = Session.PREFIX + "Id";

		protected NoUniqueDocument(Database d, lotus.domino.Document doc) {
			super(d, doc);
		}
		
		@Override
		protected NoUniqueDocument afterCreate() {
			setForm(FORM_NAME);
			return this;
		}
	}

	static class UniqueDocument extends DefaultDocument implements Unique {
		protected final static String FORM_NAME = Session.PREFIX + "Unique";
		protected final static String FIELD_ID = Session.PREFIX + "Id";

		protected UniqueDocument(Database d, lotus.domino.Document doc) {
			super(d, doc);
		}
		
		public static String getIndexName() {
			return Session.PREFIX + "Unique_Index";
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
		public org.riverframework.lotusnotes.Document setId(String id) {
			setField(FIELD_ID, id);
			return this;
		}

		@Override
		public org.riverframework.lotusnotes.Document generateId() {
			// Do nothing
			return this;
		}
	}
	
	@Test
	public void testId() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		UniqueDocument unique = rDatabase.createDocument(UniqueDocument.class);
		assertTrue("The document must be open.", unique.isOpen());
		
		RandomString rs = new RandomString(10);

		String key = rs.nextString();
		unique.setId(key).save();
		
		String oldKey = unique.getId();
		assertTrue("The Id retrieved is different to the saved.", key.equals(oldKey));
		
		unique = null;
		unique = rDatabase.getDocument(UniqueDocument.class, key);
		assertTrue("A document that was created, could not be opened with the key '" + key + "'.", unique.isOpen());		

		unique = null;
		unique = rDatabase.getDocument(UniqueDocument.class, "%%%THIS DOCUMENT DOES NOT EXIST%%%");
		assertFalse("A document that should not be found, is opened.", unique.isOpen());
		
		unique = null;
		unique = rDatabase.getDocument(UniqueDocument.class, true, "NEW_KEY_" + key);
		assertTrue("A document that should be created, is not opened.", unique.isOpen());
		
		oldKey = unique.getId();
		assertTrue("A document created by 'createIfDoesNotExist', has a wrong Id.", oldKey.equals("NEW_KEY_" + key));
		
	}
}
