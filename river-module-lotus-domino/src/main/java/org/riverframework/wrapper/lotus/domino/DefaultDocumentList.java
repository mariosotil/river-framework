package org.riverframework.wrapper.lotus.domino;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.DocumentList;

class DefaultDocumentList extends ArrayList<Document> implements DocumentList {
	private static final long serialVersionUID = -8572804835705647207L;

	private org.riverframework.wrapper.Session _session = null;
	
	public DefaultDocumentList(org.riverframework.wrapper.Session s, DocumentIterator _iterator) {
		_session = s;
		
		for(Document _doc : _iterator) {
			this.add(_doc);
		}
	}

	@Override
	public DocumentIterator iterator() {
		DocumentIterator _iterator = new DefaultDocumentIterator(_session, this);
		
		return _iterator;		
	}
	
	@Override
	public DocumentList deleteAll() {
		for (Document doc : this) {
			doc.delete();
		}
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
