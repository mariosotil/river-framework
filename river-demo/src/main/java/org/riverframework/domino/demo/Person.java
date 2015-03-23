package org.riverframework.domino.demo;

import org.openntf.domino.Document;
import org.riverframework.domino.Database;
import org.riverframework.domino.DefaultDocument;
import org.riverframework.domino.Unique;

class Person extends DefaultDocument implements Unique {

	protected Person(Database d, Document doc) {
		super(d, doc);
	}

	public static String getIndexName() {
		return "vi_ap_people_index";
	}

	public String getId() {
		return getFieldAsString("ca_pe_name");
	}

	public org.riverframework.domino.Document generateId() {
		// Do nothing
		return this;
	}

	public org.riverframework.domino.Document setId(String arg0) {
		setField("ca_pe_name", arg0);
		return this;
	}

}
