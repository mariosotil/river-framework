package org.riverframework.core;

import org.riverframework.Database;

/**
 * It is used to manage Documents. It is used if we don't need to create specific classes for each document type.
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultDocument extends AbstractDocument<DefaultDocument> implements org.riverframework.Document {
	protected DefaultDocument(Database d, org.riverframework.module.Document _d) {
		super(d, _d);
	}

	@Override
	protected DefaultDocument getThis() {
		return this;
	}
}
