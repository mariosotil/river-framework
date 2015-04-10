package org.riverframework.core;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.Database;

/**
 * It is a collection of Documents. Uses all the operations from ArrayList and allows only objects that
 * implements the Document interface. 
 * 
 * @author mario.sotil@gmail.com
 *
 */
public abstract class AbstractDocumentList extends ArrayList<org.riverframework.Document>
implements org.riverframework.DocumentList {
	private static final long serialVersionUID = -5032050258891587783L;
	protected Database database;

	protected AbstractDocumentList(Database d, org.riverframework.module.DocumentList _col) {
		database = d;
		for (org.riverframework.module.Document _doc : _col) {
			org.riverframework.Document doc = database.getDocument(_doc);
			this.add(doc);
		}
	}

	@Override
	public org.riverframework.Database getDatabase() {
		return database;
	}

	@Override
	public org.riverframework.DocumentList deleteAll() {
		for (org.riverframework.Document doc : this) {
			doc.delete();
		}
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
