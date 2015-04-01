package org.riverframework.module;

import java.util.List;

public interface DocumentCollection extends List<Document> {
	public DocumentCollection deleteAll();
}
