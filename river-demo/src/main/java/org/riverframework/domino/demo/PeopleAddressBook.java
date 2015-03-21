package org.riverframework.domino.demo;

import org.openntf.domino.Document;
import lotus.domino.NotesThread;

import org.riverframework.domino.*;

/*
 * To run this demo, you will need:
 * 
 * - IBM Notes installed in your computer
 * - A NSF database called PeopleAddressBook.nsf 
 * 
 * This database must have this design elements:
 * 
 * - A form called "People | fo_ap_people"
 * - A view called "People Index | vi_ap_people_index"
 * 
 * The form must have these fields:
 * 
 * - ca_pe_name
 * - ca_pe_address
 * - ca_pe_email
 * - ca_pe_phone
 * 
 * The view must have this one column:
 * 
 * - "Name", with values from the field ca_pe_name, ordered in ascending form
 * 
 * The view must have this selection formula:
 * 
 * - SELECT Form = "fo_ap_people"
 * 
 */

public class PeopleAddressBook {
	private static final String filepath = "PeopleAddressBook.nsf";

	static class People extends DefaultDocument implements Unique {

		protected People(Database d, Document doc) {
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

	public static void main(String[] args) {
		NotesThread.sinitThread();

		Session session = DefaultSession.getInstance().open(Credentials.getPassword());
		Database database = session.getDatabase(DefaultDatabase.class, "", filepath);

	
		System.out.println("User=" + session.getUserName());
		System.out.println("Database=" + database.getName());

		NotesThread.stermThread();
	}
}
