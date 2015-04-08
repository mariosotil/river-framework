package org.riverframework.core;

import org.riverframework.Database;

/**
 * It is used to manage collections of Documents. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultDocumentCollection extends AbstractDocumentCollection 
implements org.riverframework.DocumentCollection {
	private static final long serialVersionUID = -3371532017518314494L;

	protected DefaultDocumentCollection(Database d, org.riverframework.module.DocumentCollection _col) {
		super (d, _col);
	}
}
