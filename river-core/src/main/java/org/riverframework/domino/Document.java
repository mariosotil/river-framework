package org.riverframework.domino;


/*
 * Loads an document
 * <p>
 * This is a javadoc test
 * 
 *  @author mario.sotil@gmail.com
 *  @version 0.0.x
 */

public interface Document extends org.riverframework.Document {
	public static final String FIELD_CLASS = Session.PREFIX + "Class";
	public static final String FIELD_ID = Session.PREFIX + "Id";
	public static final boolean FORCE_SAVE = true;

	public String getForm();

	public String getUniversalId();

	public org.riverframework.domino.Document setForm(String form);

	@Override
	public org.riverframework.domino.Document setField(String field, Object value);

	@Override
	public org.riverframework.domino.Document setModified(boolean m);

	@Override
	public org.riverframework.domino.Document remove();

	@Override
	public org.riverframework.domino.Document save(boolean force);

	@Override
	public org.riverframework.domino.Document save();

	@Override
	public org.riverframework.domino.Document recalc();

	@Override
	public org.riverframework.domino.Database getDatabase();

}
