package org.riverframework.core.lotus.domino;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.core.AbstractDocument;
import org.riverframework.core.Credentials;
import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentIterator;
import org.riverframework.Session;
import org.riverframework.Unique;
import org.riverframework.View;

public abstract class AbstractStressTest {
	private final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Context context = null;
	protected Session session = null;

	protected static long maxDocumentsForStressTest = 1000;

	@Before
	public void open() {
		// Opening the test context in the current package
		try {
			if (context == null) {
				Class<?> clazz = Class.forName(this.getClass().getPackage().getName() + ".Context");
				if (Context.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					context = (Context) constructor.newInstance();
				}

				session = context.getSession();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void close() {
		context.closeSession();
	}

	static class Book extends AbstractDocument<Book> implements Unique {
		protected Book(Database d, org.riverframework.wrapper.Document<lotus.domino.Base> doc) {
			super(d, doc);
		}

		@Override
		public String getIndexName() {
			return "vi_books";
		}

		@Override
		public org.riverframework.Document generateId() {
			// Do nothing
			return this;
		}

		@Override
		public org.riverframework.Document setId(String id) {
			// Do nothing
			return this;
		}

		@Override
		public String getId() {
			return null;
		}

		@Override
		protected Book getThis() {
			return this;
		}

	}

	static class Library extends org.riverframework.core.AbstractDatabase {
		protected Library(Session s, org.riverframework.wrapper.Database<lotus.domino.Base> obj) {
			super(s, obj);
		}

		@Override
		public Class<? extends Document> getDocumentClass(org.riverframework.wrapper.Document<?> doc) {
			String form = doc.getFieldAsString("Form").toLowerCase();

			if (form.equals("fo_book")) {
				return Book.class;
			}

			return null;
		}
	}

	@Test
	public void testStress1() {
		long timeTest01Round01 = 0;
		long timeTest01Round02 = 0;
		long timeTest01Round03 = 0;
		long timeTest02Round01 = 0;
		long timeTest02Round02 = 0;
		long timeTest02Round03 = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		String suffixDb = sdf.format(new Date()) + ".nsf";

		String form = "FORM_" + sdf.format(new Date());

		long start = 0;
		long end = 0;

		final int MODULE = 100;
		final long STEP = maxDocumentsForStressTest / 20;
		int i = 0;

		try {
			lotus.domino.Session __session = lotus.domino.NotesFactory.createSession((String) null, (String) null, Credentials.getPassword());
			lotus.domino.DbDirectory dir = __session.getDbDirectory(context.getTestDatabaseServer());
			lotus.domino.Database __database = dir.createDatabase("TEST_DB_1_" + suffixDb);

			start = System.nanoTime();
			for (i = 0; i < (maxDocumentsForStressTest); i++) {
				lotus.domino.Document __doc = __database.createDocument();
				__doc.replaceItemValue("Form", form);
				__doc.replaceItemValue("Value", i % MODULE);
				__doc.save(true, false);
				__doc.recycle();


				if(i % STEP == 0) { 
					log.fine("Processed=" + i);
				}
			}

			end = System.nanoTime();
			timeTest01Round01 = (end - start) / 1000000;			


			lotus.domino.DocumentCollection __col = __database.getAllDocuments();
			start = System.nanoTime();

			i = 0;
			lotus.domino.Document __doc = __col.getFirstDocument();
			while(__doc != null) {
//				__doc.replaceItemValue("Value", i * 2);
//				__doc.save(true, false);

				if(i % STEP == 0) { 
					log.fine("Processed=" + i);
				}

				lotus.domino.Document __old = __doc;
				__doc = __col.getNextDocument(__doc);

				__old.recycle();
				i++;
			}

			end = System.nanoTime();
			timeTest02Round01 = (end - start) / 1000000;			

		} catch (NotesException e) {
			throw new RiverException(e);
		} 


		Database database = session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_2_" + suffixDb);

		start = System.nanoTime();
		//		long time0 = 0;
		//		long time1 = 0;
		//		long time2 = 0;
		//		long time3 = 0;
		//		long time4 = 0;
		for (i = 0; i < (maxDocumentsForStressTest); i++) {
			@SuppressWarnings("unused")
			Document doc = database.createDocument()
					.setField("Form", form)
					.setField("Value", i % MODULE)
					.save();

			//			time0 = System.nanoTime();
			//			Document doc = database.createDocument();
			//			time1 = System.nanoTime();
			//			doc.setField("Form", form);
			//			time2 = System.nanoTime();
			//			doc.setField("Value", i % MODULE);
			//			time3 = System.nanoTime();
			//			doc.save();
			//			time4 = System.nanoTime();
			//
			//			log.fine("time1=" + ((time1 - time0)));
			//			log.fine("time2=" + ((time2 - time0)));
			//			log.fine("time3=" + ((time3 - time0)));
			//			log.fine("time4=" + ((time4 - time0)));

			if(i % STEP == 0) { 
				log.fine("Processed=" + i);
			}

		}

		end = System.nanoTime();


		timeTest01Round02 = (end - start) / 1000000;			

		DocumentIterator iterator = database.getAllDocuments();

		i = 0;
		start = System.nanoTime();
		for (@SuppressWarnings("unused") Document doc: iterator) {
			// doc.setField("Value", i * 4).save();

			if(i % STEP == 0) { 
				log.fine("Processed=" + i);
			}
			i++;
		}
		end = System.nanoTime();
		timeTest02Round02 = (end - start) / 1000000;			


		Database library = session.createDatabase(Library.class, context.getTestDatabaseServer(), "TEST_LIBRARY_" + suffixDb);
		@SuppressWarnings("unused")
		View books = library.createView("vi_books", "SELECT Form=\"fo_book\"");

		start = System.nanoTime();
		//		long time0 = 0;
		//		long time1 = 0;
		//		long time2 = 0;
		//		long time3 = 0;
		//		long time4 = 0;
		for (i = 0; i < (maxDocumentsForStressTest); i++) {
			@SuppressWarnings("unused")
			Document doc = library.createDocument()
					.setField("Form", "fo_book")
					.setField("Value", i % MODULE)
					.save();

			//			time0 = System.nanoTime();
			//			Document doc = library.createDocument();
			//			time1 = System.nanoTime();
			//			doc.setField("Form", "fo_ap_book");
			//			time2 = System.nanoTime();
			//			doc.setField("Value", i % MODULE);
			//			time3 = System.nanoTime();
			//			doc.save();
			//			time4 = System.nanoTime();
			//
			//			log.fine("time1=" + ((time1 - time0) ));
			//			log.fine("time2=" + ((time2 - time0) ));
			//			log.fine("time3=" + ((time3 - time0) ));
			//			log.fine("time4=" + ((time4 - time0) ));

			if(i % STEP == 0) { 
				log.fine("Processed=" + i);
			}

		}

		end = System.nanoTime();


		timeTest01Round03 = (end - start) / 1000000;			

		DocumentIterator booksIterator = library.getAllDocuments();

		i = 0;
		start = System.nanoTime();
		for (@SuppressWarnings("unused") Document doc: booksIterator) {
//			doc.setField("Value", i * 8).save();

			if(i % STEP == 0) { 
				log.fine("Processed=" + i);
			}
			i++;
		}
		end = System.nanoTime();
		timeTest02Round03 = (end - start) / 1000000;			



		double diffTest01Round02 = (((double) timeTest01Round02) / timeTest01Round01 - 1) * 100;
		final double thresholdTest01Round02 = 30;

		double diffTest02Round02 = (((double) timeTest02Round02) / timeTest02Round01 - 1) * 100;
		final double thresholdTest02Round02 = 550;

		double diffTest01Round03 = (((double) timeTest01Round02) / timeTest01Round01 - 1) * 100;
		final double thresholdTest01Round03 = 30;

		double diffTest02Round03 = (((double) timeTest02Round02) / timeTest02Round01 - 1) * 100;
		final double thresholdTest02Round03 = 800;


		String logTest01Round02 = "Test01Round02: The framework makes a time of " + timeTest01Round02 + "ms versus " +  timeTest01Round01 + "ms.";
		String logTest02Round02 = "Test02Round02: The framework makes a time of " + timeTest02Round02 + "ms versus " +  timeTest02Round01 + "ms.";

		String logTest01Round03 = "Test01Round03: The framework makes a time of " + timeTest01Round03 + "ms versus " +  timeTest01Round01 + "ms.";
		String logTest02Round03 = "Test02Round03: The framework makes a time of " + timeTest02Round03 + "ms versus " +  timeTest02Round01 + "ms.";

		log.fine(logTest01Round02);
		log.fine(logTest02Round02);

		log.fine(logTest01Round03);
		log.fine(logTest02Round03);

		assertTrue(logTest01Round02 + " That is over the " + thresholdTest01Round02 + "% allowed.", diffTest01Round02 <= thresholdTest01Round02 );
		assertTrue(logTest02Round02 + " That is over the " + thresholdTest02Round02 + "% allowed.", diffTest02Round02 <= thresholdTest02Round02 );

		assertTrue(logTest01Round03 + " That is over the " + thresholdTest01Round03 + "% allowed.", diffTest01Round03 <= thresholdTest01Round03 );
		assertTrue(logTest02Round03 + " That is over the " + thresholdTest02Round03 + "% allowed.", diffTest02Round03 <= thresholdTest02Round03 );
	}

	@Test
	public void testStress2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		String suffixDb = sdf.format(new Date()) + ".nsf";
		Database db1 = session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_1_" + suffixDb);
		assertTrue("The test database could not be instantiated.", db1 != null);
		assertTrue("The test database could not be opened.", db1.isOpen());

		Database db2 = session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_2_" + suffixDb);
		assertTrue("The test database could not be instantiated.", db2 != null);
		assertTrue("The test database could not be opened.", db2.isOpen());

		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());

		View view1 = db1.createView(name, "SELECT Form = \"" + form + "\"");
		assertTrue("There is a problem creating the view in the test database.", view1.isOpen());

		View view2 = db2.createView(name, "SELECT Form = \"" + form + "\"");
		assertTrue("There is a problem creating the view in the test database.", view2.isOpen());

		db1 = session.getDatabase(context.getTestDatabaseServer(), "TEST_DB_1_" + suffixDb);
		db2 = session.getDatabase(context.getTestDatabaseServer(), "TEST_DB_2_" + suffixDb);

		int i;
		final int MODULE = 100;

		for (i = 0; i < (maxDocumentsForStressTest); i++) {
			db1.createDocument()
			.setField("Form", form)
			.setField("Value", i % MODULE)
			.save();

			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
			}
		}

		for (i = 0; i < maxDocumentsForStressTest; i++) {
			db2.createDocument()
			.setField("Form", form)			
			.setField("Value", i % MODULE)
			.save();

			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
			}
		}

		log.fine("Updating FT indexes");
		try {
			((lotus.domino.Database) db1.getWrapperObject().getNativeObject()).updateFTIndex(true);
			((lotus.domino.Database) db2.getWrapperObject().getNativeObject()).updateFTIndex(true);

			Thread.sleep(2000);
		} catch (Exception e) {
			throw new RiverException(e);
		}

		log.fine("Stressing");

		i = 0;
		for(Document doc1: db1.getAllDocuments()) {
			int value = doc1.getFieldAsInteger("Value");
			DocumentIterator it = db2.search("FIELD Value=" + value + "");
			for(Document doc2: it) {
				log.finest(doc1.getObjectId() + ": " + (doc2.isOpen() ? "F" : "Not f") + "ound value " + value + " on " + doc2.getObjectId());
			}

			if(i % 100 == 0) { 
				log.fine("Processed=" + i);
			}
			
			i++;
		}

		log.fine("Done");
		//
		//		view1.delete();
		//		assertFalse("There is a problem deleting the last view created.", view1.isOpen());
		//
		//		view2.delete();
		//		assertFalse("There is a problem deleting the last view created.", view1.isOpen());
		//
		//		db1.delete();		
		//		assertFalse("There is a problem deleting the last database created.", db1.isOpen());
		//
		//		db2.delete();		
		//		assertFalse("There is a problem deleting the last database created.", db1.isOpen());
	}

	@Test
	public void testStress3() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		Database database = session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_" + sdf.format(new Date()) + ".nsf");

		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View view = database.createView(name, "SELECT Form = \"" + form + "\"");

		assertTrue("There is a problem creating the view in the test database.", view.isOpen());

		view.close();
		view = null;

		int i;

		for (i = 0; i < maxDocumentsForStressTest; i++) {
			database.createDocument()
			.setField("Form", form)
			.setField("Value", i)
			.save();

			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
			}
		}

		log.info("Step 1!");

		view = database.getView(name);
		assertTrue("There is a problem opening the last view created in the test database.", view.isOpen());

		DocumentIterator it = view.getAllDocuments();

		i = 0;
		for (@SuppressWarnings("unused") Document doc: it) {
			i++;
			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
			}
		}
		assertTrue("There is a problem with the documents indexed in the last view.", i == maxDocumentsForStressTest);

		log.info("Step 2!");

		it = view.getAllDocuments();

		i = 0;
		while (it.hasNext()) {
			i++;
			Document doc2 = it.next();
			doc2.delete();
			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
			}
		}

		log.info("Step 3!");

		view.refresh();
		i = 0;
		it = view.getAllDocuments();
		while (it.hasNext()) {
			i++;
			it.next();
			if(i % 500 == 0) log.fine("Processed=" + i);
		}
		log.info("Step 4!");

		assertTrue("There is a problem with the last documents created when we try to delete them.", i == 0);


		view.delete();
		assertFalse("There is a problem deleting the last view created.", view.isOpen());

		database.delete();		
		assertFalse("There is a problem deleting the last database created.", database.isOpen());
	}

}
