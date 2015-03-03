package org.riverframework.fw;

import org.riverframework.RiverException;

/*
 * Should I change this class' name to "Index"?
 * This must be in its own package "org.riverframework.lotusnotes"
 */
public abstract class AbstractView<T> implements org.riverframework.View<T> {
	protected org.riverframework.Database<?> rDatabase = null;
	protected T view = null;

	protected AbstractView(org.riverframework.Database<?> d, T obj) {
		rDatabase = d;
		view = obj;

		initIterator();
	}

	protected abstract void initIterator();

	@Override
	public boolean isOpen() {
		return view != null;
	}

	@Override
	public abstract org.riverframework.Document<?> getDocumentByKey(Object key);

	@Override
	public abstract org.riverframework.DocumentCollection<?> getAllDocumentsByKey(Object key);

	@Override
	public abstract org.riverframework.View<T> refresh();

	protected void close() {
		try {
			if (isOpen()) {
				view = null;
			}
		} catch (Exception e) {
			throw new RiverException(e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}
}
