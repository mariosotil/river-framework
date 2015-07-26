package org.riverframework.core;

/**
 * It is used to access Views of documents. Works as an index that makes easier access to the documents.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultIndex extends AbstractView<DefaultIndex> {
	protected DefaultIndex(Database database, org.riverframework.wrapper.View<?> obj) {
		super(database, obj);
	}

	@Override
	protected DefaultIndex getThis() {
		return this;
	}
}