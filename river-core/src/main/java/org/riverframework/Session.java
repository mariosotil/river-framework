package org.riverframework;

public interface Session extends Base {
	// TODO: evaluate if this const is necessary or can be removed
	/**
	 * The ELEMENT_PREFIX is used to define the of elements as Views, to indicate that elements are for exclusive use of
	 * the framework's core.
	 */
	public static final String ELEMENT_PREFIX = "RIVER_";

	// TODO: evaluate if this const is necessary or can be removed
	/**
	 * The FIELD_PREFIX is used to define field's names, to indicate that fields are for exclusive use of the
	 * framework's core.
	 */
	public static final String FIELD_PREFIX = "RIVER_";

	/**
	 * Returns a core Database object after open a wrapper Database, using the parameters indicated.
	 * 
	 * @param parameters
	 *            the parameters needed to open an existent wrapper Database. How this parameters must to be set will
	 *            depend on how the wrapper loaded is implemented.
	 * @return a core Database object
	 */
	public <U extends Database> U getDatabase(String... parameters);

	/**
	 * Returns a core Database object after open a wrapper Database, using the parameters indicated.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDatabase and implements the core Database interface.
	 * @param parameters
	 *            the parameters needed to open an existent wrapper Database. How this parameters must to be set will
	 *            depend on how the wrapper loaded is implemented.
	 * @return a core Database object
	 */
	public <U extends Database> U getDatabase(Class<U> type, String... parameters);

	/**
	 * Returns true if the wrapper was loaded and the session opened.
	 * 
	 * @return true if it's opened
	 */
	public boolean isOpen();

	/**
	 * Returns the current user name logged with this session.
	 * 
	 * @return the current user name
	 */
	public String getUserName();

	/**
	 * Close the session and frees its resources, handles, etc.
	 */
	@Override
	public void close();
}
