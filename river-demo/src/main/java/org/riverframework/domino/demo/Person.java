package org.riverframework.domino.demo;

import org.riverframework.domino.Document;
import org.riverframework.domino.Database;
import org.riverframework.domino.DefaultDocument;
import org.riverframework.domino.Unique;

class Person extends DefaultDocument implements Unique {

	protected Person(Database d, org.openntf.domino.Document doc) {
		super(d, doc);
	}

	public static String getIndexName() {
		return "vi_ap_person_index";
	}
	
	protected Document afterCreate() {
		setForm("fo_ap_person");
		return this;
	}

	public String getId() {
		return getFieldAsString("ca_pe_id");
	}

	public org.riverframework.domino.Document generateId() {
		long id = database.getCounter("PERSON").getCount();
		setField("ca_pe_id", String.valueOf(id));
		return this;
	}

	public org.riverframework.domino.Document setId(String arg0) {
		throw new UnsupportedOperationException();
	}

}
