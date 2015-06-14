package org.riverframework.wrapper.lotus.domino;

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
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

public abstract class AbstractStressTest {
	final String TEST_FORM = "TestForm";
	final String TEST_VIEW = "TestView";
	final String TEST_GRAPH = "TestGraph";

	private final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Context context = null;
	protected Session<lotus.domino.Base> _session = null;

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

				_session = (Session<lotus.domino.Base>) context.getSession().getWrapperObject();
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
	public void testStress() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		Database<lotus.domino.Base> database = _session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_" + sdf.format(new Date()) + ".nsf");

		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View<lotus.domino.Base> view = database.createView(name, "SELECT Form = \"" + form + "\"");

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

		DocumentIterator<lotus.domino.Base> it = view.getAllDocuments();

		i = 0;
		for (@SuppressWarnings("unused") org.riverframework.wrapper.Document<lotus.domino.Base> doc: it) {
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
			Document<lotus.domino.Base> doc2 = it.next();
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
