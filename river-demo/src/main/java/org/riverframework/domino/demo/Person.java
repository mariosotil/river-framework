package org.riverframework.domino.demo;

import org.riverframework.impl.Document;
import org.riverframework.impl.Database;
import org.riverframework.impl.DefaultDocument;
import org.riverframework.impl.Unique;

class Person extends DefaultDocument implements Unique {

	protected Person(Database d, org.riverframework.wrapper.Document doc) {
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

	public Document generateId() {
		long id = database.getCounter("PERSON").getCount();
		setField("ca_pe_id", String.valueOf(id));
		return this;
	}

	public Document setId(String arg0) {
		throw new UnsupportedOperationException();
	}

}
