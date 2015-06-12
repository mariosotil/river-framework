package org.riverframework.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.Field;
import org.riverframework.RandomString;
import org.riverframework.Session;

public abstract class AbstractDocumentTest {
	protected Session session = null;
	protected Context context = null;
	protected Database database = null;
	protected Database complexDatabase = null;

	final String TEST_FORM = "TestForm";

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				String className = this.getClass().getPackage().getName() + ".Context";
				Class<?> clazz = Class.forName(className);
				if (org.riverframework.Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = context.getSession();
				database = session.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());
				complexDatabase = session
						.getDatabase(DefaultDatabase.class, context.getTestDatabaseServer(), context.getTestDatabasePath());

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

	@Test
	public void testCompareFieldValue() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MILLISECOND, 0);
		Date testDate = calendar.getTime();

		doc.setField("FIELD_STRING", "SOME_TEXT")
				.setField("FIELD_INTEGER", 100)
				.setField("FIELD_DOUBLE_WITHOUT_DECIMALS", 100.0)
				.setField("FIELD_DOUBLE_WITH_DECIMALS", 100.55)
				.setField("FIELD_DATE", testDate)
				.setField("FIELD_STRING_ARRAY", new String[] { "VAL1", "VAL2" })
				.setField("FIELD_OBJECT",
						new DefaultField(Arrays.asList(new String[] { "VAL1", "VAL2" })));

		assertTrue("The String saved must be equal to the test value",
				doc.compareFieldValue("FIELD_STRING", "SOME_TEXT"));
		assertFalse("The String saved must be different to a random value",
				doc.compareFieldValue("FIELD_STRING", "SODIUHOUIFD"));

		assertTrue("The Integer saved must be equal to the test value",
				doc.compareFieldValue("FIELD_INTEGER", 100));
		assertFalse("The Integer saved must be different to a random value",
				doc.compareFieldValue("FIELD_INTEGER", 3847));

		assertTrue("The Double WITHOUT decimals saved must be equal to the test value",
				doc.compareFieldValue("FIELD_DOUBLE_WITHOUT_DECIMALS", 100.0));
		assertFalse("The Double WITHOUT decimals saved must be different to a random value",
				doc.compareFieldValue("FIELD_DOUBLE_WITHOUT_DECIMALS", 100.89));

		assertTrue("The Double WITH decimals saved must be equal to the test value",
				doc.compareFieldValue("FIELD_DOUBLE_WITH_DECIMALS", 100.55));
		assertFalse("The Double WITH decimals saved must be different to a random value",
				doc.compareFieldValue("FIELD_DOUBLE_WITH_DECIMALS", 100.89));

		assertTrue("The Date saved must be equal to the test value",
				doc.compareFieldValue("FIELD_DATE", testDate));
		calendar.set(2012, 1, 15, 5, 25, 12);
		Date randomDate = calendar.getTime();
		assertFalse("The Date saved must be different to a random value",
				doc.compareFieldValue("FIELD_DATE", randomDate));

		assertTrue("The String array saved must be equal to the test value",
				doc.compareFieldValue("FIELD_STRING_ARRAY", new String[] { "VAL1", "VAL2" }));
		assertFalse("The String array saved must be different to a random value",
				doc.compareFieldValue("FIELD_STRING_ARRAY", new String[] { "VALWW", "VALZZ" }));

		assertTrue(
				"The Vector saved must be equal to the test value",
				doc.compareFieldValue("FIELD_OBJECT",
						new String[] { "VAL1", "VAL2" }));
		assertFalse(
				"The Vector saved must be different to a random value",
				doc.compareFieldValue("FIELD_OBJECT",
						new String[] { "VAL1", "VALZZ" }));
	}

	@Test
	public void testSetAndGetFieldAsString() {
		RandomString rs = new RandomString(10);

		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		String testValue = rs.nextString();
		doc.setField(testField, testValue);
		String newValue = doc.getFieldAsString(testField);

		assertTrue("The value retrieved was different to the saved", newValue.equals(testValue));

		doc.setField(testField, 20);
		String strValue = doc.getFieldAsString(testField);
		int intValue = doc.getFieldAsInteger(testField);

		assertFalse("The integer value can not be retrieved as string. ", "20".equals(strValue));
		assertTrue("The integer value can be retrieved as integer. ", 20 == intValue);
	}

	@Test
	public void testSetAndGetFieldAsInteger() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		int testValue = 1000;
		doc.setField(testField, testValue);
		int newValue = doc.getFieldAsInteger(testField);

		assertTrue("The value retrieved was different to the saved", newValue == testValue);
	}

	@Test
	public void testSetAndGetFieldAsLong() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		long testValue = 1000100010L;
		doc.setField(testField, testValue);
		long newValue = doc.getFieldAsLong(testField);

		assertTrue("The value retrieved was different to the saved", newValue == testValue);
	}

	@Test
	public void testSetAndGetFieldAsDouble() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		Double testValue = 100.45;
		doc.setField(testField, testValue);
		Double newValue = doc.getFieldAsDouble(testField);

		assertTrue("The value retrieved was different to the saved",
				Double.compare(newValue, testValue) == 0);
	}

	@Test
	public void testSetAndGetFieldAsDate() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		Date testValue = new Date();
		doc.setField(testField, testValue);
		Date newValue = doc.getFieldAsDate(testField);

		// Lotus Notes does not save the milliseconds
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(testValue);
		calendar.set(Calendar.MILLISECOND, 0);
		testValue = calendar.getTime();

		assertTrue("The value retrieved was different to the saved", newValue.compareTo(testValue) == 0);
	}

	@Test
	public void testSetAndGetFieldAsArrayString() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		doc.setField(testField, new String[] { "VAL1", "VAL2" });
		Field newValue = doc.getField(testField);

		assertTrue("The Array saved is different to the Array retrieved", newValue.size() == 2
				&& newValue.get(0).equals("VAL1") && newValue.get(1).equals("VAL2"));
	}

	@Test
	public void testSetAndGetFieldAsVector() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		doc.setField(testField, new String[] { "VAL1", "VAL2" });
		Field newValue = doc.getField(testField);

		assertTrue("The Vector saved is different to the Vector retrieved", newValue.size() == 2
				&& newValue.get(0).equals("VAL1") && newValue.get(1).equals("VAL2"));
	}

	@Test
	public void testIsModified() {
		RandomString rs = new RandomString(10);

		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("A document was created but it is set as modified", doc.isModified());

		doc.setField("Form", TEST_FORM);

		String testField = DefaultSession.PREFIX + "TestSetField";
		String testValue = rs.nextString();
		doc.setField(testField, testValue);

		assertTrue("The document was modified but its flag is still false", doc.isModified());

		doc.save();
		assertFalse("The document was saved but it is still set as modified", doc.isModified());

		doc.setField(testField, testValue);
		assertFalse("A document's field was set with the same value, but now the document it's set as modified", doc.isModified());

		String anotherValue = rs.nextString();
		doc.setField(testField, anotherValue);
		assertTrue("The document was modified but its flag is still false", doc.isModified());

		doc.save();
		assertFalse("The document was saved but it is still set as modified", doc.isModified());
	}

	@Test
	public void testGetIsFieldEmpty() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());

		doc.setField("Form", TEST_FORM);
		doc.setField("THIS_FIELD_EXISTS", "SOME_VALUE");

		assertTrue("An inexistent field was not detected.",
				doc.isFieldEmpty("THIS_FIELD_DOES_NOT_EXIST"));
		assertFalse("An existent field was not detected.", doc.isFieldEmpty("THIS_FIELD_EXISTS"));
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
	public void testHasField() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
		assertFalse("The document is new and is set as a conflict.", doc.hasField("THIS_FIELD_DOES_NOT_EXIST"));

		doc.setField("Form", TEST_FORM);
		doc.setField("THIS_FIELD_DOES_NOT_EXIST", "");

		assertTrue("The document is a conflict and was not detected.", doc.hasField("THIS_FIELD_DOES_NOT_EXIST"));
	}

	@Test
	public void testIsOpen() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document doc = database.getDocument(DefaultDocument.class);

		assertFalse("The document is not being detected as NOT open.", doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document could not be created", doc.isOpen());
	}

	@Test
	public void testIsNew() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		assertTrue("The document is new and is not detected like that.", doc.isNew());
	}

	@Test
	public void testGetUniversalId() {
		assertTrue("The test database could not be opened.", database.isOpen());

		DefaultDocument doc = database.createDocument(DefaultDocument.class);
		String uniqueId = doc.getObjectId();

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
		assertTrue("The test database could not be opened.", database.isOpen());

		RandomString rs = new RandomString(10);
		String uniqueId = "";
		DefaultDocument doc = database.createDocument(DefaultDocument.class);

		doc.setField("Form", TEST_FORM);
		uniqueId = doc.getObjectId();

		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertFalse("A document that was not saved was found in the database.", doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);
		uniqueId = doc.getObjectId();
		doc.save(false);
		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertFalse(
				"A document was created, NOT MODIFIED and saved with NOT FORCE parameter was found in the database.",
				doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);
		uniqueId = doc.getObjectId();
		doc.save();
		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertFalse(
				"A document was created, NOT MODIFIED and saved with A IMPLICIT NOT FORCE parameter was found in the database.",
				doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);
		uniqueId = doc.getObjectId();
		doc.save(false);
		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertFalse(
				"A document was created, NOT MODIFIED and saved with NOT FORCE parameter was found in the database.",
				doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);
		uniqueId = doc.getObjectId();
		doc.save(true);
		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, NOT MODIFIED and saved with FORCE parameter was not found in the database.",
				doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);
		uniqueId = doc.getObjectId();
		doc.save(true);
		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, NOT MODIFIED and saved with FORCE parameter was not found in the database.",
				doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);
		uniqueId = doc.getObjectId();
		doc.setField("SOME_TEST_FIELD", rs.nextString());
		doc.save(false);
		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, MODIFIED and saved with NOT FORCE parameter was not found in the database.",
				doc.isOpen());

		doc = null;
		doc = database.createDocument(DefaultDocument.class);
		uniqueId = doc.getObjectId();
		doc.setField("SOME_TEST_FIELD", rs.nextString());
		doc.save(true);
		doc = null;
		doc = database.getDocument(DefaultDocument.class, uniqueId);

		assertTrue(
				"A document was created, MODIFIED and saved with FORCE parameter was not found in the database.",
				doc.isOpen());
	}

	// Classes to testing the implementations
	static class SimpleRequest extends AbstractDocument<SimpleRequest> {
		protected SimpleRequest(Database d, org.riverframework.wrapper.Document<?> _d) {
			super(d, _d);
		}

		@Override
		protected SimpleRequest internalRecalc() {
			super.internalRecalc();

			RandomString rs = new RandomString(10);
			setField("CALCULATED", rs.nextString());

			return this;
		}

		@Override
		protected SimpleRequest getThis() {
			return this;
		}
	}

	static class ComplexDatabase extends AbstractDatabase {

		protected ComplexDatabase(Session s, org.riverframework.wrapper.Database<?> obj) {
			super(s, obj);
		}
	}

	@Test
	public void testInternalRecalc() {
		assertTrue("The test database Beach.nsf could not be opened as a ComplexDatabase .",
				complexDatabase.isOpen());

		SimpleRequest simple = complexDatabase.createDocument(SimpleRequest.class);
		simple.recalc();
		String calculated = simple.getFieldAsString("CALCULATED");
		String non_existent = simple.getFieldAsString("NON_EXISTENT");

		assertTrue("A non-existent field returned the value '" + non_existent + "'.",
				non_existent.equals(""));
		assertFalse("There is a problem with the recalc() method.", calculated.equals(""));
	}

}
