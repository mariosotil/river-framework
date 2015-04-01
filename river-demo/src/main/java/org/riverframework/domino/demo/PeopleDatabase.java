package org.riverframework.domino.demo;

import org.riverframework.core.DefaultDatabase;
import org.riverframework.Session;
import org.riverframework.module.Database;

public class PeopleDatabase extends DefaultDatabase {

	protected PeopleDatabase(Session s, Database obj) {
		super(s, obj);
	}

	@Override
	public Class<? extends org.riverframework.Document> detectClass(org.riverframework.module.Document _doc) {
		String form = _doc.getFieldAsString("Form");
		if (form.equals("fo_ap_person")) 
			return Person.class;

		return null;
	}
}
