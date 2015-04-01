package org.riverframework.core;

import java.util.ArrayList;

import org.riverframework.Database;

public class DefaultDocumentCollection extends ArrayList<org.riverframework.Document>
		implements org.riverframework.DocumentCollection {
	private static final long serialVersionUID = -5032050258891587783L;
	protected Database database;

	public DefaultDocumentCollection(Database d) {
		database = d;
	}

	@Override
	public org.riverframework.DocumentCollection loadFrom(org.riverframework.module.DocumentCollection _col) {
		clear();
		for (org.riverframework.module.Document _doc : _col) {
			org.riverframework.Document doc = database.getDocument(_doc);
			this.add(doc);
		}

		return this;
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
}
