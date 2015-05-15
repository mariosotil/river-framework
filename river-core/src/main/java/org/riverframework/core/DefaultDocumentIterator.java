package org.riverframework.core;

import org.riverframework.Database;

/**
 * It is used to manage collections of Documents. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultDocumentIterator extends AbstractDocumentIterator 
implements org.riverframework.DocumentIterator {
	protected DefaultDocumentIterator(Database d, org.riverframework.wrapper.DocumentIterator _iterator) {
		super (d, _iterator);
	}
}
