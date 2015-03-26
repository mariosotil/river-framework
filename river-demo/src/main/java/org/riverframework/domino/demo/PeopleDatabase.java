package org.riverframework.domino.demo;

import org.riverframework.impl.DefaultDatabase;
import org.riverframework.impl.DefaultDocument;
import org.riverframework.impl.Session;
import org.riverframework.wrapper.Database;

public class PeopleDatabase extends DefaultDatabase {

	protected PeopleDatabase(Session s, Database obj) {
		super(s, obj);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends org.riverframework.impl.Document> U getDocument(Class<U> clazz, org.riverframework.wrapper.Document doc) {
		U rDoc = null;
		Class<U> c = clazz;

		if (c == null) {
			// If there's no a explicit class...
			String form = doc.getFieldAsString("Form");
			if (form.equals("fo_ap_person")) {
				c = (Class<U>) Person.class;
			} else {			
				c = (Class<U>) DefaultDocument.class;
			}
		} 
		
		rDoc = super.getDocument(c, doc);
		return rDoc;
	}
}
