package org.riverframework.extended;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;
import org.riverframework.core.Database;
import org.riverframework.core.Document;
import org.riverframework.core.Session;
import org.riverframework.extended.AbstractDatabase;
import org.riverframework.extended.AbstractDocument;

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
				database = session.getDatabase(context.getTestDatabaseServer(), context.getTestDatabasePath());
				complexDatabase = session
						.getDatabase(context.getTestDatabaseServer(), context.getTestDatabasePath());

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

	// Classes to testing the implementations
	static class SimpleRequest extends AbstractDocument<SimpleRequest> {

		protected SimpleRequest(Document doc) {
			super(doc);
		}

		protected SimpleRequest internalRecalc() {
			RandomString rs = new RandomString(10);
			setField("CALCULATED", rs.nextString());

			return this;
		}

		@Override
		protected SimpleRequest getThis() {
			return this;
		}
	}

	static class ComplexDatabase extends AbstractDatabase<ComplexDatabase> {

		protected ComplexDatabase(Database database) {
			super(database);
		}

		@Override
		protected ComplexDatabase getThis() {
			return this;
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
