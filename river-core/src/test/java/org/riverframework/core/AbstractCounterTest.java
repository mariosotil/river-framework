package org.riverframework.core;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riverframework.Context;
import org.riverframework.RandomString;

public abstract class AbstractCounterTest {

  protected Session session = null;
  protected IndexedDatabase database = null;
  protected Context context = null;

  @Before
  public void open() {
    // Opening the test context in the current package
    try {
      if (context == null) {
        String className = this.getClass().getPackage().getName()
            + ".Context";
        Class<?> clazz = Class.forName(className);
        if (org.riverframework.Context.class.isAssignableFrom(clazz)) {
          Constructor<?> constructor = clazz.getDeclaredConstructor();
          constructor.setAccessible(true);
          context = (Context) constructor.newInstance();
        }

        session = context.getSession();
        database = session.getDatabase(UniqueDatabase.class,
            context.getTestDatabaseServer(),
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
  public void testCounter() {
    assertTrue("The test database could not be opened.", database.isOpen());

    RandomString rs = new RandomString(10);

    String key = rs.nextString();
    DefaultCounter counter = database.getCounter(key);

    long n = counter.getCount();
    long n1 = counter.getCount();

    assertTrue(
        "The counter does not return two consecutives counters for the key '"
            + key + "'. It returned n=" + n + " and n1=" + n1,
        n1 == n + 1);

    for (int i = 0; i < 10; i++) {
      n = counter.getCount();
    }

    DocumentIterator it = database.getIndex(DefaultCounter.class)
        .getAllDocumentsByKey(key);

    int num = 0;
    while (it.hasNext()) {
      num++;
      it.next();
    }

    assertTrue(
        "There is a problem with the counter. It was found zero or more than one document in the Counter index with the key "
            + key, num == 1);

  }

  static class UniqueDatabase extends AbstractIndexedDatabase<UniqueDatabase> {

    protected UniqueDatabase(Session session,
        org.riverframework.wrapper.Database<?> _database) {
      super(session, _database);

      registerDocumentClass(DefaultCounter.class);
    }

    @Override
    protected UniqueDatabase getThis() {
      return this;
    }
  }
}
