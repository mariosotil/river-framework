package org.riverframework.wrapper;

import java.util.Iterator;

public interface DocumentIterator<N> extends Base<N>, Iterator<Document<N>>,	Iterable<Document<N>> {
	@Override
	public DocumentIterator<N> iterator();

//	public DocumentList asDocumentList();
	
	public DocumentIterator<N> deleteAll();
}
