package org.riverframework.wrapper.org.openntf.domino;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

public abstract class AbstractStressTest {
	private final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Context context = null;
	protected Session<org.openntf.domino.Base> _session = null;

	protected static long maxDocumentsForStressTest = 1000;

	@SuppressWarnings("unchecked")
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

				_session = (Session<org.openntf.domino.Base>) context.getSession().getWrapperObject();
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
	public void testStress1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		String suffixDb = sdf.format(new Date()) + ".nsf";
		Database<org.openntf.domino.Base> db1 = _session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_1_" + suffixDb);
		assertTrue("The test database could not be instantiated.", db1 != null);
		assertTrue("The test database could not be opened.", db1.isOpen());

		Database<org.openntf.domino.Base> db2 = _session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_2_" + suffixDb);
		assertTrue("The test database could not be instantiated.", db2 != null);
		assertTrue("The test database could not be opened.", db2.isOpen());

		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());

		View<org.openntf.domino.Base> view1 = db1.createView(name, "SELECT Form = \"" + form + "\"");
		assertTrue("There is a problem creating the view in the test database.", view1.isOpen());

		View<org.openntf.domino.Base> view2 = db2.createView(name, "SELECT Form = \"" + form + "\"");
		assertTrue("There is a problem creating the view in the test database.", view2.isOpen());
				
		db1 = _session.getDatabase(context.getTestDatabaseServer(), "TEST_DB_1_" + suffixDb);
		db2 = _session.getDatabase(context.getTestDatabaseServer(), "TEST_DB_2_" + suffixDb);
		
		int i;
		final int MODULE = 100;
		
		for (i = 0; i < (maxDocumentsForStressTest); i++) {
			db1.createDocument()
			.setField("Form", form)
			.setField("Value", i % MODULE)
			.save();

			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
				_session.getFactory().logStatus();
			}
		}

		for (i = 0; i < maxDocumentsForStressTest; i++) {
			db2.createDocument()
			.setField("Form", form)			
			.setField("Value", i % MODULE)
			.save();

			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
				_session.getFactory().logStatus();
			}
		}

		log.fine("Updating FT indexes");
		try {
			((org.openntf.domino.Database) db1.getNativeObject()).updateFTIndex(true);
			((org.openntf.domino.Database) db2.getNativeObject()).updateFTIndex(true);
			
			Thread.sleep(2000);
		} catch (Exception e) {
			throw new RiverException(e);
		}
		
		log.fine("Stressing");
		
		i = 0;
		for(Document<org.openntf.domino.Base> doc1: db1.getAllDocuments()) {
			int value = doc1.getFieldAsInteger("Value");
			DocumentIterator<org.openntf.domino.Base> it = db2.search("FIELD Value=" + value + "");
			for(Document<org.openntf.domino.Base> doc2: it) {
				log.finest(doc1.getObjectId() + ": " + (doc2.isOpen() ? "F" : "Not f") + "ound value " + value + " on " + doc2.getObjectId());
			}
			
			if(i++ % 100 == 0) { 
				log.fine("Processed=" + i);
				_session.getFactory().logStatus();
			}
		}
		
		log.fine("Done");

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
	public void testStress2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		Database<org.openntf.domino.Base> database = _session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_" + sdf.format(new Date()) + ".nsf");

		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View<org.openntf.domino.Base> view = database.createView(name, "SELECT Form = \"" + form + "\"");

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
				_session.getFactory().logStatus();
			}
		}

		_session.getFactory().logStatus();
		log.info("Step 1!");

		view = database.getView(name);
		assertTrue("There is a problem opening the last view created in the test database.", view.isOpen());

		DocumentIterator<org.openntf.domino.Base> it = view.getAllDocuments();

		i = 0;
		for (@SuppressWarnings("unused") org.riverframework.wrapper.Document<org.openntf.domino.Base> doc: it) {
			i++;
			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
				_session.getFactory().logStatus();
			}
		}
		assertTrue("There is a problem with the documents indexed in the last view.", i == maxDocumentsForStressTest);

		_session.getFactory().logStatus();
		log.info("Step 2!");

		it = view.getAllDocuments();

		i = 0;
		while (it.hasNext()) {
			i++;
			Document<org.openntf.domino.Base> doc2 = it.next();
			doc2.delete();
			if(i % 500 == 0) { 
				log.fine("Processed=" + i);
				_session.getFactory().logStatus();
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
		_session.getFactory().logStatus();
		log.info("Step 4!");

		assertTrue("There is a problem with the last documents created when we try to delete them.", i == 0);


		view.delete();
		assertFalse("There is a problem deleting the last view created.", view.isOpen());

		database.delete();		
		assertFalse("There is a problem deleting the last database created.", database.isOpen());
	}
	
}
