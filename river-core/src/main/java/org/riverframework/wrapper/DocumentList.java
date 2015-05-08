package org.riverframework.wrapper;

import java.util.List;

public interface DocumentList extends List<Document> {
	@Override
	public DocumentIterator iterator();

	public DocumentList deleteAll();
}
