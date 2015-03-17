package org.riverframework.lotusnotes;

public interface View extends org.riverframework.View {
	@Override
	public org.riverframework.lotusnotes.DocumentCollection getAllDocumentsByKey(Object key);

	@Override
	public org.riverframework.lotusnotes.View refresh();

	@Override
	public org.riverframework.lotusnotes.Document next();

}
