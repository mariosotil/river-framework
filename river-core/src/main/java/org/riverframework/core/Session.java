package org.riverframework.core;

public interface Session extends Base {

  /**
   * The PREFIX is used to define names for views, documents or classes, to indicate that those
   * elements are for exclusive use of the framework's core.
   */
  public static final String PREFIX = "RIVER_";

  /**
   * Returns the object that wraps the native object. For example, if the wrapper loaded is
   * River.LOTUS_DOMINO, and the object is an instance of DefaultDocument, getNativeObject() will
   * return an object that implements the org.riverframework.wrapper.Document interface.
   *
   * @return the object used to wrap the native object
   */
  @Override
  org.riverframework.wrapper.Session<?> getWrapperObject();

  /**
   * Creates a new database.
   *
   * @param location Depends on what wrapper is being used.
   * @return a DefaultDatabase object.
   */
  public Database createDatabase(String... location);

  /**
   * Creates a new database.
   *
   * @param <U> a class that inherits from AbstractDatabase and implements Database.
   * @param clazz The class that implements org.riverframework.Database
   * @param location Depends on what wrapper is being used.
   * @return an object from the class selected in the parameter 'type'
   */
  public <U extends AbstractDatabase<?>> U createDatabase(Class<U> clazz,
      String... location);

  /**
   * Returns a core Database object after open a wrapper Database, using the parameters indicated.
   *
   * @param location the location needed to open an existent wrapper Database. How this parameters
   * must to be set will depend on how the wrapper loaded is implemented.
   * @return a core Database object
   */
  public Database getDatabase(String... location);

  /**
   * Returns a core Database object after open a wrapper Database, using the parameters indicated.
   *
   * @param <U> a class that inherits from AbstractDatabase and implements Database.
   * @param clazz a class that inherits from DefaultDatabase and implements the core Database
   * interface.
   * @param location the location needed to open an existent wrapper Database. How this parameters
   * must to be set will depend on how the wrapper loaded is implemented.
   * @return a core Database object
   */
  public <U extends AbstractDatabase<?>> U getDatabase(Class<U> clazz,
      String... location);

  /**
   * Returns the current user name logged with this session.
   *
   * @return the current user name
   */
  public String getUserName();

  public void cleanUp();

  /**
   * Close the session and frees its resources, handles, etc.
   */
  @Override
  public void close();
}
