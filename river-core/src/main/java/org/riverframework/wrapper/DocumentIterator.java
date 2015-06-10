package org.riverframework.wrapper;

import java.util.Iterator;

public interface DocumentIterator extends Base, Iterator<Document>,	Iterable<Document> {
	@Override
	public DocumentIterator iterator();

//	public DocumentList asDocumentList();
	
	public DocumentIterator deleteAll();
}
