package org.riverframework.wrapper.lotus.domino;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.logging.Logger;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.Session;
import org.riverframework.wrapper.View;

public abstract class AbstractBaseTest {
	// private final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Context context = null;
	protected Session<lotus.domino.Base> _session = null;

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
	public void testRecycling() {
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

		Document<lotus.domino.Base> _doc = database.createDocument()
		.setField("Form", form)
		.setField("Value", 100)
		.save();

		lotus.domino.Document __doc = (lotus.domino.Document) _doc.getNativeObject();
		_doc = null;
		
		System.gc();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RiverException(e);
		}
		
		_session.getFactory().cleanUp();
		
		boolean isRecycled = DefaultDocument.isRecycled(__doc);

		assertTrue("The document was no recycled.", isRecycled);

		view = database.getView(name);
		view.delete();
		assertFalse("There is a problem deleting the last view created.", view.isOpen());

		database.delete();		
		assertFalse("There is a problem deleting the last database created.", database.isOpen());
	}
}
