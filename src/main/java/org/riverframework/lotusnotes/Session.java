package org.riverframework.lotusnotes;

public interface Session extends org.riverframework.Session {
	public lotus.domino.Session getNotesSession();

	@Override
	public org.riverframework.lotusnotes.Session open(String... parameters);

}
