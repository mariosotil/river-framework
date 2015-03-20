package org.riverframework.domino;

public interface View extends org.riverframework.View {
	@Override
	public org.riverframework.domino.DocumentCollection getAllDocumentsByKey(Object key);

	@Override
	public org.riverframework.domino.View refresh();

	@Override
	public org.riverframework.domino.Document next();

}
