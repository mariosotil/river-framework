package org.riverframework.wrapper;

import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.View;

public interface Factory {
	public Database getDatabase(Object obj);
	
	public Document getDocument(Object obj);
	
	public View getView(Object obj);
	
	public DocumentIterator getDocumentIterator(Object obj);
}
