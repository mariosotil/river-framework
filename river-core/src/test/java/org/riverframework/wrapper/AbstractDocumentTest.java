package org.riverframework.wrapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;
import org.riverframework.core.Field;

public abstract class AbstractDocumentTest {
	protected Session<?> session = null;
	protected Database<?> database = null;

	final String TEST_FORM = "TestForm";

	protected Context context = null;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				Class<?> clazz = Class.forName(this.getClass().getPackage()
						.getName()
						+ ".Context");
				if (Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = context.getSession().getWrapperObject();
				database = session.getDatabase(context.getTestDatabaseServer(),
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

	@Test
	public void testSetAndGetFieldAsString() {
		RandomString rs = new RandomString(10);

		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		String testField = "TestSetField";
		String testValue = rs.nextString();
		_doc.setField(testField, testValue);
		String newValue = _doc.getFieldAsString(testField);

		assertTrue("The value retrieved was different to the saved",
				newValue.equals(testValue));

		_doc.setField(testField, 20);
		String strValue = _doc.getFieldAsString(testField);
		int intValue = _doc.getFieldAsInteger(testField);

		assertTrue("The integer value can not be retrieved as string. ",
				"20".equals(strValue) || "20.0".equals(strValue));
		assertTrue("The integer value can not be retrieved as integer. ",
				20 == intValue);
	}

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private String getHash(String text) {
		String hash = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(text.getBytes());
			hash = bytesToHex(digest.digest());
		} catch (Exception e) {
			// Do nothing. Just return an empty string.
		}
		return hash;
	}

	@Test
	public void testSetAndGetFieldAsBigString() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		String testField = "TestBigField";

		StringBuilder sb = new StringBuilder(100 * 1024);
		for (int i = 0; i < 10 * 1024; i++) {
			sb.append("0123456789");
		}
		String testValue = sb.toString();
		String hash1 = getHash(testValue);
		assertFalse(
				"It could not be possible to get the hash from the test text.",
				hash1.equals(""));

		_doc.setField(testField, testValue);
		_doc.save();

		String savedValue = _doc.getFieldAsString(testField);
		String hash2 = getHash(savedValue);
		assertFalse(
				"It could not be possible to get the hash from the saved text.",
				hash2.equals(""));

		assertTrue("The value retrieved was different to the saved",
				hash1.equals(hash2));
	}

	@Test
	public void testSetAndGetFieldAsInteger() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		String testField = "TestSetField";
		int testValue = 1000;
		_doc.setField(testField, testValue);
		int newValue = _doc.getFieldAsInteger(testField);

		assertTrue("The value retrieved was different to the saved",
				newValue == testValue);
	}

	@Test
	public void testSetAndGetFieldAsLong() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		String testField = "TestSetField";
		long testValue = 1000100010L;
		_doc.setField(testField, testValue);
		long newValue = _doc.getFieldAsLong(testField);

		assertTrue("The value retrieved was different to the saved",
				newValue == testValue);
	}

	@Test
	public void testSetAndGetFieldAsDouble() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		String testField = "TestSetField";
		Double testValue = 100.45;
		_doc.setField(testField, testValue);
		Double newValue = _doc.getFieldAsDouble(testField);

		assertTrue("The value retrieved was different to the saved",
				Double.compare(newValue, testValue) == 0);
	}

	@Test
	public void testSetAndGetFieldAsDate() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		Calendar calendar = Calendar.getInstance();

		String testField = "TestSetField";
		calendar.setTime(new Date());
		calendar.set(Calendar.MILLISECOND, 0); // Databases as Lotus Notes does
												// not support the milliseconds
		Date testValue = calendar.getTime();

		_doc.setField(testField, testValue);
		Date newValue = _doc.getFieldAsDate(testField);

		calendar.setTime(testValue);
		testValue = calendar.getTime();

		assertTrue("The value retrieved was different to the saved",
				newValue.compareTo(testValue) == 0);
	}

	@Test
	public void testSetAndGetFieldAsArrayString() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		String testField = "TestSetField";
		_doc.setField(testField, new String[] { "VAL1", "VAL2" });
		Field newValue = _doc.getField(testField);

		assertTrue("The Array saved is different to the Array retrieved",
				newValue.size() == 2 && newValue.get(0).equals("VAL1")
						&& newValue.get(1).equals("VAL2"));
	}

	@Test
	public void testSetAndGetFieldAsVector() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);

		String testField = "TestSetField";
		_doc.setField(testField, new String[] { "VAL1", "VAL2" });
		Field newValue = _doc.getField(testField);

		assertTrue("The Vector saved is different to the Vector retrieved",
				newValue.size() == 2 && newValue.get(0).equals("VAL1")
						&& newValue.get(1).equals("VAL2"));
	}

	@Test
	public void testGetIsFieldEmpty() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());

		_doc.setField("Form", TEST_FORM);
		_doc.setField("THIS_FIELD_EXISTS", "SOME_VALUE");

		assertTrue("An inexistent field was not detected.",
				_doc.isFieldEmpty("THIS_FIELD_DOES_NOT_EXIST"));
		assertFalse("An existent field was not detected.",
				_doc.isFieldEmpty("THIS_FIELD_EXISTS"));
	}

	@Test
	public void testIsOpen() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.getDocument();

		assertFalse("The document is not being detected as NOT open.",
				_doc.isOpen());

		_doc = null;
		_doc = database.createDocument();

		assertTrue("The document could not be created", _doc.isOpen());
	}

	@Test
	public void testIsNew() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();

		assertTrue("The document is new and is not detected like that.",
				_doc.isNew());
	}

	@Test
	public void testGetUniversalId() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument();
		String uniqueId = _doc.getObjectId();

		assertFalse("It could not be retrieved de document's unique id.",
				uniqueId.equals(""));
	}

	public void testSave() {
		assertTrue("The test database could not be opened.", database.isOpen());

		RandomString rs = new RandomString(10);
		String uniqueId = "";
		Document<?> _doc = database.createDocument();

		_doc.setField("Form", TEST_FORM);
		uniqueId = _doc.getObjectId();

		_doc = null;
		_doc = database.getDocument(uniqueId);

		assertFalse("A document that was not saved was found in the database.",
				_doc.isOpen());

		_doc = null;
		_doc = database.createDocument();
		uniqueId = _doc.getObjectId();
		_doc.save();
		_doc = null;
		_doc = database.getDocument(uniqueId);

		assertTrue(
				"A document was created, NOT MODIFIED and saved was not found in the database.",
				_doc.isOpen());

		_doc = null;
		_doc = database.createDocument();
		uniqueId = _doc.getObjectId();
		_doc.setField("SOME_TEST_FIELD", rs.nextString());
		_doc.save();
		_doc = null;
		_doc = database.getDocument(uniqueId);

		assertTrue(
				"A document was created, MODIFIED and saved was not found in the database.",
				_doc.isOpen());
	}

	@Test
	public void testRecalc() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Document<?> _doc = database.createDocument().setField("Form", TEST_FORM);
		_doc.setField("CALCULATED_FROM_FORM", "TEMPORAL_DATA");

		String calculated = _doc.getFieldAsString("CALCULATED_FROM_FORM");
		assertTrue(
				"There is a problem setting the value for the field CALCULATED_FROM_FORM.",
				calculated.equals("TEMPORAL_DATA"));

		_doc.recalc();
		calculated = _doc.getFieldAsString("CALCULATED_FROM_FORM");
		assertTrue("There is a problem with the recalc() method.",
				calculated.equals("ROGER"));

		String non_existent = _doc.getFieldAsString("NON_EXISTENT");
		assertTrue("A non-existent field returned the value '" + non_existent
				+ "'.", non_existent.equals(""));
	}

	@Test
	public void testGetFields() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Calendar cal1 = Calendar.getInstance();
		cal1.set(2015, 03, 15);

		Date date1 = cal1.getTime();

		Document<?> _doc = database.createDocument().setField("Form", TEST_FORM)
				.setField("String", "HI!").setField("Date", date1)
				.setField("Number", 75.3)
				.setField("StringArray", new String[] { "A", "B", "C" })
				.setField("Empty", "").save();

		Map<String, Field> fields = _doc.getFields();

		assertTrue("The String value retrieved was different to the saved",
				fields.get("String").get(0).equals("HI!"));

		Date date2 = (Date) fields.get("Date").get(0);
		long l1 = Long.valueOf(date1.getTime() / 1000).intValue();
		long l2 = Long.valueOf(date2.getTime() / 1000).intValue();
		assertTrue("The Date value [" + date2.toString()
				+ "] retrieved was different to the saved [" + date1.toString()
				+ "]", l1 == l2);

		assertTrue("The Number value retrieved was different to the saved",
				((Double) fields.get("Number").get(0)).compareTo(75.3) == 0);
		assertTrue("The String array retrieved was different to the saved",
				fields.get("StringArray").get(2).equals("C"));
		assertTrue(
				"The Empty String value retrieved was different to the saved",
				fields.get("Empty").get(0).equals(""));

	}

	@Test
	public void testCompareFields() {
		assertTrue("The test database could not be opened.", database.isOpen());

		Calendar cal1 = Calendar.getInstance();
		cal1.set(2015, 03, 15);

		Random rand = new Random();

		for (int i = 0; i < 20; i++) {
			Date date1 = cal1.getTime();

			try {
				Thread.sleep(rand.nextInt(300) + 1);
			} catch (InterruptedException e) {
				// Do nothing
			}

			Date date2 = cal1.getTime();

			Document<?> _doc = database.createDocument()
					.setField("Form", TEST_FORM).setField("Date1", date1)
					.setField("Number1", 30).setField("Text1", "hey!");

			try {
				Thread.sleep(rand.nextInt(300) + 1);
			} catch (InterruptedException e) {
				// Do nothing
			}

			_doc.setField("Date2", date2).setField("Number2", 30)
					.setField("Text2", "hey!").save();

			Date date10 = _doc.getFieldAsDate("Date1");
			Date date20 = _doc.getFieldAsDate("Date2");

			assertTrue("The dates saved are not equal", date10.equals(date20));

			int number10 = _doc.getFieldAsInteger("Number1");
			int number20 = _doc.getFieldAsInteger("Number2");

			assertTrue("The numbers saved are not equal", number10 == number20);

			String text10 = _doc.getFieldAsString("Text1");
			String text20 = _doc.getFieldAsString("Text2");

			assertTrue("The text saved are not equal", text10.equals(text20));

			Field field10 = _doc.getField("Date1");
			Field field20 = _doc.getField("Date2");

			assertTrue("The date as fields objects are not equal",
					field10.equals(field20));
		}
	}

	final String BigString = "";
}
