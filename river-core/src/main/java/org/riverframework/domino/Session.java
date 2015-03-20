package org.riverframework.domino;

public interface Session extends org.riverframework.Session {
	public org.openntf.domino.Session getNotesSession();

	@Override
	public org.riverframework.domino.Session open(String... parameters);

}
