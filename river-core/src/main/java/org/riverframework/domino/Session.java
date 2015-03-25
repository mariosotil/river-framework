package org.riverframework.domino;

public interface Session extends org.riverframework.Session {
	public lotus.domino.DateTime Java2NotesTime(java.util.Date d);

	@Override
	public org.riverframework.domino.Session open(String... parameters);

}
