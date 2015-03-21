package org.riverframework.domino;

public interface Session extends org.riverframework.Session {
	public org.openntf.domino.DateTime Java2NotesTime(java.util.Date d);

	@Override
	public org.riverframework.domino.Session open(String... parameters);

}
