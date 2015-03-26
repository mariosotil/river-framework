package org.riverframework.wrapper;

import java.util.List;

public interface DocumentCollection extends List<Document> {
	public DocumentCollection deleteAll();
}
