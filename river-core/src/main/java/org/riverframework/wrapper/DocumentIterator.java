package org.riverframework.wrapper;

import java.util.Iterator;

public interface DocumentIterator<N, D> extends Base<N>, Iterator<Document<D>>, Iterable<Document<D>> {
	@Override
	public DocumentIterator<N, D> iterator();

	public DocumentIterator<N, D> deleteAll();
}
