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
	 * This method returns a core Database object after open a module Database, using the parameters indicated.
	 * 
	 * @param parameters
	 *            the parameters needed to open an existent module Database. How this parameters must to be set will
	 *            depend on how the module loaded is implemented.
	 * @return a core Database object
	 */
	public <U extends Database> U getDatabase(String... parameters);

	/**
	 * This method returns a core Database object after open a module Database, using the parameters indicated.
	 * 
	 * @param clazz
	 *            a class that inherits from DefaultDatabase and implements the core Database interface.
	 * @param parameters
	 *            the parameters needed to open an existent module Database. How this parameters must to be set will
	 *            depend on how the module loaded is implemented.
	 * @return a core Database object
	 */
	public <U extends Database> U getDatabase(Class<U> type, String... parameters);

	/**
	 * If the core Session object is created, but the module Session is null or can't be opened, this method will
	 * return false.
	 * 
	 * @return true if the module Session is opened
	 */
	public boolean isOpen();

	/**
	 * This method should returns the current user name logged with this session. Its behavior will depend on how the
	 * module loaded is implemented.
	 * 
	 * @return the current user name
	 */
	public String getUserName();

	/**
	 * This method close the session and frees its resources, etc. Its behavior will depend on how the module loaded is
	 * implemented.
	 */
	@Override
	public void close();
}
