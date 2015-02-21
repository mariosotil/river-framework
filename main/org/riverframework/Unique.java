package org.riverframework;

public interface Unique {
	/**
	 * This is where the document's primary key will be saved
	 */
	public static final String FIELD_ID = Session.PREFIX + "Id";

	public org.riverframework.Unique calcId();

	public String getId();

}
