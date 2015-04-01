package org.riverframework;

import java.util.List;

public interface DocumentCollection extends List<org.riverframework.Document> {
	public Database getDatabase();

	public DocumentCollection loadFrom(org.riverframework.module.DocumentCollection _col);

	public DocumentCollection deleteAll();
}
