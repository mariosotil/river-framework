package org.riverframework.wrapper;

import java.util.Iterator;

public interface DocumentIterator extends Iterator<Document>,
		Iterable<Document> {
	@Override
	public DocumentIterator iterator();

	public DocumentList asDocumentList();
	
	public DocumentIterator deleteAll();
}
