package org.riverframework.core;

import org.riverframework.Database;

/**
 * It is used to manage collections of Documents. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public final class DefaultDocumentList extends AbstractDocumentList 
implements org.riverframework.DocumentList {
	private static final long serialVersionUID = -3371532017518314494L;

	protected DefaultDocumentList(Database d, org.riverframework.module.DocumentList _col) {
		super (d, _col);
	}
}
