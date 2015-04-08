package org.riverframework.domino.demo;

import org.riverframework.Document;
import org.riverframework.Database;
import org.riverframework.core.AbstractDocument;
import org.riverframework.Unique;

class Person extends AbstractDocument<Person> implements Unique {

	protected Person(Database d, org.riverframework.module.Document doc) {
		super(d, doc);
	}

	public String getIndexName() {
		return "vi_ap_person_index";
	}
	
	protected Person afterCreate() {
		setField("Form", "fo_ap_person");
		return this;
	}

	public String getId() {
		return getFieldAsString("ca_pe_id");
	}

	public Person generateId() {
		long id = database.getCounter("PERSON").getCount();
		setField("ca_pe_id", String.valueOf(id));
		return this;
	}

	public Document setId(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Person getThis() {
		return this;
	}

}
