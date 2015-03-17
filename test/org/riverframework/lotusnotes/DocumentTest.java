package org.riverframework.lotusnotes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import lotus.domino.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.RiverException;
import org.riverframework.lotusnotes.base.DefaultDatabase;
import org.riverframework.lotusnotes.base.DefaultDocument;
import org.riverframework.lotusnotes.base.DefaultSession;

public class DocumentTest {
	final Session session = DefaultSession.getInstance();
	private Database rDatabase = null;
	private ComplexDatabase rComplexDatabase = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void init() {
		try {
			session.open(Context.getServer(), Context.getUser(), Context.getPassword());

			rDatabase = session.getDatabase(DefaultDatabase.class, Context.getServer(), Context.getDatabase());
			rComplexDatabase = session.getDatabase(ComplexDatabase.class, Context.getServer(), Context.getDatabase());

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@After
	public void close() {
		session.close();
	}

	@Test
	public void testCompareFieldValue() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MILLISECOND, 0);
		Date testDate = calendar.getTime();

		rDoc.setField("FIELD_STRING", "SOME_TEXT")
				.setField("FIELD_INTEGER", 100)
				.setField("FIELD_DOUBLE_WITHOUT_DECIMALS", 100.0)
				.setField("FIELD_DOUBLE_WITH_DECIMALS", 100.55)
				.setField("FIELD_DATE", testDate)
				.setField("FIELD_STRING_ARRAY", new String[] { "VAL1", "VAL2" })
				.setField("FIELD_STRING_VECTOR",
						new Vector<String>(Arrays.asList(new String[] { "VAL1", "VAL2" })));

		assertTrue("The String saved must be equal to the test value",
				rDoc.compareFieldValue("FIELD_STRING", "SOME_TEXT"));
		assertFalse("The String saved must be different to a random value",
				rDoc.compareFieldValue("FIELD_STRING", "SODIUHOUIFD"));

		assertTrue("The Integer saved must be equal to the test value",
				rDoc.compareFieldValue("FIELD_INTEGER", 100));
		assertFalse("The Integer saved must be different to a random value",
				rDoc.compareFieldValue("FIELD_INTEGER", 3847));

		assertTrue("The Double WITHOUT decimals saved must be equal to the test value",
				rDoc.compareFieldValue("FIELD_DOUBLE_WITHOUT_DECIMALS", 100.0));
		assertFalse("The Double WITHOUT decimals saved must be different to a random value",
				rDoc.compareFieldValue("FIELD_DOUBLE_WITHOUT_DECIMALS", 100.89));

		assertTrue("The Double WITH decimals saved must be equal to the test value",
				rDoc.compareFieldValue("FIELD_DOUBLE_WITH_DECIMALS", 100.55));
		assertFalse("The Double WITH decimals saved must be different to a random value",
				rDoc.compareFieldValue("FIELD_DOUBLE_WITH_DECIMALS", 100.89));

		assertTrue("The Date saved must be equal to the test value",
				rDoc.compareFieldValue("FIELD_DATE", testDate));
		calendar.set(2012, 1, 15, 5, 25, 12);
		Date randomDate = calendar.getTime();
		assertFalse("The Date saved must be different to a random value",
				rDoc.compareFieldValue("FIELD_DATE", randomDate));

		assertTrue("The String array saved must be equal to the test value",
				rDoc.compareFieldValue("FIELD_STRING_ARRAY", new String[] { "VAL1", "VAL2" }));
		assertFalse("The String array saved must be different to a random value",
				rDoc.compareFieldValue("FIELD_STRING_ARRAY", new String[] { "VALWW", "VALZZ" }));

		assertTrue(
				"The Vector saved must be equal to the test value",
				rDoc.compareFieldValue("FIELD_STRING_VECTOR",
						new Vector<Object>(Arrays.asList(new String[] { "VAL1", "VAL2" }))));
		assertFalse(
				"The Vector saved must be different to a random value",
				rDoc.compareFieldValue("FIELD_STRING_VECTOR",
						new Vector<Object>(Arrays.asList(new String[] { "VAL1", "VALZZ" }))));
	}

	@Test
	public void testSetAndGetFieldAsString() {
		RandomString rs = new RandomString(10);

		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		String testValue = rs.nextString();
		rDoc.setField(testField, testValue);
		String newValue = rDoc.getFieldAsString(testField);

		assertTrue("The value retrieved was different to the saved", newValue.equals(testValue));
	}

	@Test
	public void testSetAndGetFieldAsInteger() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		int testValue = 1000;
		rDoc.setField(testField, testValue);
		int newValue = rDoc.getFieldAsInteger(testField);

		assertTrue("The value retrieved was different to the saved", newValue == testValue);
	}

	@Test
	public void testSetAndGetFieldAsDouble() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		Double testValue = 100.45;
		rDoc.setField(testField, testValue);
		Double newValue = rDoc.getFieldAsDouble(testField);

		assertTrue("The value retrieved was different to the saved",
				Double.compare(newValue, testValue) == 0);
	}

	@Test
	public void testSetAndGetFieldAsDate() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		Date testValue = new Date();
		rDoc.setField(testField, testValue);
		Date newValue = rDoc.getFieldAsDate(testField);

		// Lotus Notes does not save the milliseconds
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(testValue);
		calendar.set(Calendar.MILLISECOND, 0);
		testValue = calendar.getTime();

		assertTrue("The value retrieved was different to the saved", newValue.compareTo(testValue) == 0);
	}

	@Test
	public void testSetAndGetFieldAsArrayString() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		rDoc.setField(testField, new String[] { "VAL1", "VAL2" });
		Vector<Object> newValue = rDoc.getField(testField);

		assertTrue("The Array saved is different to the Array retrieved", newValue.size() == 2
				&& newValue.elementAt(0).equals("VAL1") && newValue.elementAt(1).equals("VAL2"));
	}

	@Test
	public void testSetAndGetFieldAsVector() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		rDoc.setField(testField, new Vector<Object>(Arrays.asList(new String[] { "VAL1", "VAL2" })));
		Vector<Object> newValue = rDoc.getField(testField);

		assertTrue("The Vector saved is different to the Vector retrieved", newValue.size() == 2
				&& newValue.elementAt(0).equals("VAL1") && newValue.elementAt(1).equals("VAL2"));
	}

	@Test
	public void testIsModified() {
		RandomString rs = new RandomString(10);

		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("A document was created but it is set as modified", rDoc.isModified());

		rDoc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		String testValue = rs.nextString();
		rDoc.setField(testField, testValue);

		assertTrue("The document was modified but its flag is still false", rDoc.isModified());

		rDoc.save();
		assertFalse("The document was saved but it is still set as modified", rDoc.isModified());

		rDoc.setField(testField, testValue);
		assertFalse("A document's field was set with the same value, but now the document it's set as modified", rDoc.isModified());

		String anotherValue = rs.nextString();
		rDoc.setField(testField, anotherValue);
		assertTrue("The document was modified but its flag is still false", rDoc.isModified());

		rDoc.save();
		assertFalse("The document was saved but it is still set as modified", rDoc.isModified());
	}

	@Test
	public void testGetIsFieldEmpty() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());

		rDoc.setField("Form", TEST_FORM);
		rDoc.setField("THIS_FIELD_EXISTS", "SOME_VALUE");

		assertTrue("An inexistent field was not detected.",
				rDoc.isFieldEmpty("THIS_FIELD_DOES_NOT_EXIST"));
		assertFalse("An existent field was not detected.", rDoc.isFieldEmpty("THIS_FIELD_EXISTS"));
	}

	/*
	 * @Test
	 * public void testSetAndGetIsPublic() {
	 * assertTrue("The test database could not be opened.", rDatabase.isOpen());
	 * 
	 * DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);
	 * 
	 * assertTrue("The document could not be created", rDoc.isOpen());
	 * 
	 * rDoc.setAsPublic(true);
	 * assertTrue("The document could not be set as Public", rDoc.isPublic());
	 * 
	 * rDoc.setAsPublic(false);
	 * assertFalse("The document could not be set as NOT Public", rDoc.isPublic());
	 * }
	 * 
	 * @Test
	 * public void testGetDocumentClass() {
	 * assertTrue("The test database could not be opened.", rDatabase.isOpen());
	 * 
	 * DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);
	 * 
	 * assertTrue("The document could not be created", rDoc.isOpen());
	 * 
	 * rDoc.setField(DefaultDocument.FIELD_CLASS, "SOME_FORM");
	 * 
	 * String form = rDoc.getDocumentClass();
	 * 
	 * assertFalse("It can not be retrieved the field Form.", form.equals(""));
	 * }
	 */

	@Test
	public void testIsConflict() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
		assertFalse("The document is new and is set as a conflict.", rDoc.isConflict());

		rDoc.setField("Form", TEST_FORM);
		rDoc.setField("$Conflict", "");

		assertTrue("The document is a conflict and was not detected.", rDoc.isConflict());
	}

	@Test
	public void testIsOpen() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.getDocument(DefaultDocument.class);

		assertFalse("The document is not being detected as NOT open.", rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", rDoc.isOpen());
	}

	@Test
	public void testIsNew() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		assertTrue("The document is new and is not detected like that.", rDoc.isNew());
	}

	@Test
	public void testGetUniversalId() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);
		String uniqueId = rDoc.getUniversalId();

		assertFalse("It could not be retrieved de document's unique id.", uniqueId.equals(""));
	}

	/*
	 * @Test
	 * public void testMarkAndUnmarkAndIsDeleted() {
	 * assertTrue("The test database could not be opened.", rDatabase.isOpen());
	 * 
	 * DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);
	 * 
	 * rDoc.markDeleted();
	 * 
	 * assertTrue("The document could not be marked as deleted.", rDoc.isDeleted());
	 * 
	 * rDoc.unmarkDeleted();
	 * 
	 * assertFalse("The document could not be unmarked as deleted.", rDoc.isDeleted());
	 * }
	 */
	public void testSave() {
		assertTrue("The test database could not be opened.", rDatabase.isOpen());

		RandomString rs = new RandomString(10);
		String uniqueId = "";
		DefaultDocument rDoc = rDatabase.createDocument(DefaultDocument.class);

		rDoc.setField("Form", TEST_FORM);
		uniqueId = rDoc.getUniversalId();

		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertFalse("A document that was not saved was found in the database.", rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);
		uniqueId = rDoc.getUniversalId();
		rDoc.save(false);
		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertFalse(
				"A document was created, NOT MODIFIED and saved with NOT FORCE parameter was found in the database.",
				rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);
		uniqueId = rDoc.getUniversalId();
		rDoc.save();
		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertFalse(
				"A document was created, NOT MODIFIED and saved with A IMPLICIT NOT FORCE parameter was found in the database.",
				rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);
		uniqueId = rDoc.getUniversalId();
		rDoc.save(false);
		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertFalse(
				"A document was created, NOT MODIFIED and saved with NOT FORCE parameter was found in the database.",
				rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);
		uniqueId = rDoc.getUniversalId();
		rDoc.save(true);
		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, NOT MODIFIED and saved with FORCE parameter was not found in the database.",
				rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);
		uniqueId = rDoc.getUniversalId();
		rDoc.save(true);
		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, NOT MODIFIED and saved with FORCE parameter was not found in the database.",
				rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);
		uniqueId = rDoc.getUniversalId();
		rDoc.setField("SOME_TEST_FIELD", rs.nextString());
		rDoc.save(false);
		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, MODIFIED and saved with NOT FORCE parameter was not found in the database.",
				rDoc.isOpen());

		rDoc = null;
		rDoc = rDatabase.createDocument(DefaultDocument.class);
		uniqueId = rDoc.getUniversalId();
		rDoc.setField("SOME_TEST_FIELD", rs.nextString());
		rDoc.save(true);
		rDoc = null;
		rDoc = rDatabase.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, MODIFIED and saved with FORCE parameter was not found in the database.",
				rDoc.isOpen());
	}

	// Classes to testing the implementations
	static class SimpleRequest extends DefaultDocument {
		protected SimpleRequest(Database d, Document doc) {
			super(d, doc);
		}

		@Override
		protected DefaultDocument internalRecalc() {
			super.internalRecalc();

			RandomString rs = new RandomString(10);
			setField("CALCULATED", rs.nextString());

			return this;
		}
	}

	static class ComplexDatabase extends org.riverframework.lotusnotes.base.DefaultDatabase {

		protected ComplexDatabase(Session s, lotus.domino.Database obj) {
			super(s, obj);
		}

		public ComplexDatabase(Session s, String... location) {
			super(s, location);
		}
	}

	@Test
	public void testInternalRecalc() {
		assertTrue("The test database Beach.nsf could not be opened as a ComplexDatabase .",
				rComplexDatabase.isOpen());

		SimpleRequest simple = rComplexDatabase.createDocument(SimpleRequest.class);
		simple.recalc();
		String calculated = simple.getFieldAsString("CALCULATED");
		String non_existent = simple.getFieldAsString("NON_EXISTENT");

		assertTrue("A non-existent field returned the value '" + non_existent + "'.",
				non_existent.equals(""));
		assertFalse("There is a problem with the recalc() method.", calculated.equals(""));
	}

}
