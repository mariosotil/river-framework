package org.riverframework.core;

import org.riverframework.Database;

/**
 * It is used to access Views of documents. Works as an index that makes easier access to the documents. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultView extends AbstractView implements org.riverframework.View {
	protected DefaultView(Database d, org.riverframework.wrapper.View<?> obj) {
		super(d, obj);
	}
}
