package org.riverframework.lotusnotes;


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

	public org.riverframework.lotusnotes.Document setForm(String form);

	@Override
	public org.riverframework.lotusnotes.Document setField(String field, Object value);

	@Override
	public org.riverframework.lotusnotes.Document generateId();

	@Override
	public org.riverframework.lotusnotes.Document setModified(boolean m);

	@Override
	public org.riverframework.lotusnotes.Document remove();

	@Override
	public org.riverframework.lotusnotes.Document save(boolean force);

	@Override
	public org.riverframework.lotusnotes.Document save();

	@Override
	public org.riverframework.lotusnotes.Document recalc();

	@Override
	public org.riverframework.lotusnotes.Database getDatabase();

}
