package org.riverframework.fw;

import org.riverframework.RiverException;

/*
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public abstract class AbstractDocumentCollection<T> implements org.riverframework.DocumentCollection<T> {
	protected org.riverframework.Database<?> rDatabase;
	protected T col = null;

	public AbstractDocumentCollection(org.riverframework.Database<?> d, T c) {
		try {
			rDatabase = d;
			col = c;
			initIterator();

		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	protected abstract void initIterator();

	@Override
	public abstract boolean hasNext();

	@Override
	public abstract org.riverframework.Document<?> next();

	@Override
	public abstract void remove();

}
