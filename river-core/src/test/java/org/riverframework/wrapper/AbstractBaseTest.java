package org.riverframework.wrapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RiverException;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//import java.util.logging.Logger;

public abstract class AbstractBaseTest {
	// private final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

	protected Context context = null;
	protected Session<local.mock.Base> _session = null;

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

				_session = (Session<local.mock.Base>) context.getSession().getWrapperObject();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void close() {
		context.closeSession();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRecycling() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		Database<local.mock.Database> database =
				(Database<local.mock.Database>) _session.createDatabase(context.getTestDatabaseServer(), "TEST_DB_" + sdf.format(new Date()) + ".nsf");

		assertTrue("The test database could not be instantiated.", database != null);
		assertTrue("The test database could not be opened.", database.isOpen());

		String name = "VIEW_" + sdf.format(new Date());
		String form = "FORM_" + sdf.format(new Date());
		View<local.mock.View> view = (View<local.mock.View>) database.createView(name, "SELECT Form = \"" + form + "\"");

		assertTrue("There is a problem creating the view in the test database.", view.isOpen());

		view.close();
		view = null;

		Document<local.mock.Document> _doc =
				(Document<local.mock.Document>) database.createDocument()
				.setField("Form", form)
				.setField("Value", 100)
				.save();

		local.mock.Document __doc = (local.mock.Document) _doc.getNativeObject();
		_doc = null;

		System.gc();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RiverException(e);
		}

		_session.getFactory().cleanUp();

		boolean isRecycled = AbstractBaseDatabase.isObjectRecycled(__doc);

		assertTrue("The document was no recycled.", isRecycled);

		view = (View<local.mock.View>) database.getView(name);
		view.delete();
		assertFalse("There is a problem deleting the last view created.", view.isOpen());

		database.delete();		
		assertFalse("There is a problem deleting the last database created.", database.isOpen());
	}
}
