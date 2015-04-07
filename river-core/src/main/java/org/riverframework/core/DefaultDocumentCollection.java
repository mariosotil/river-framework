package org.riverframework.core;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.Database;

public class DefaultDocumentCollection extends ArrayList<org.riverframework.Document>
implements org.riverframework.DocumentCollection {
	private static final long serialVersionUID = -5032050258891587783L;
	protected Database database;

	protected DefaultDocumentCollection(Database d, org.riverframework.module.DocumentCollection _col) {
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
	public org.riverframework.DocumentCollection deleteAll() {
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
