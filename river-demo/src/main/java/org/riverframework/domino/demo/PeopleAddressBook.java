package org.riverframework.domino.demo;

import lotus.domino.NotesThread;

import org.riverframework.Database;
import org.riverframework.Document;
import org.riverframework.DocumentCollection;
import org.riverframework.River;
import org.riverframework.Session;
import org.riverframework.core.Credentials;

/**
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
	private static final String filepath = "PeopleAddressBookX.nsf";

	public static void main(String[] args) {
		NotesThread.sinitThread();
		
		Session session = River.getInstance().getSession(River.MODULE_LOTUS_DOMINO, null, null, Credentials.getPassword());
		Database database = session.getDatabase(PeopleDatabase.class, "", filepath);

		System.out.println("User=" + session.getUserName());
		System.out.println("Database=" + database.getName());

		// Deleting everything
		database.getAllDocuments().deleteAll();

		// Creating three persons
		 
		Person jd = (Person) database.createDocument(Person.class)
		.setField("Name", "John Doe")
		.setField("Age", 35)
		.generateId()
		.save();

		String johnDoeId = jd.getId();
				
		database.createDocument(Person.class)
		.setField("Name", "Jane Doe")
		.generateId()
		.setField("Age", 29)
		.save();

		database.createDocument(Person.class)
		.generateId()
		.setField("Name", "John Smith")
		.setField("Age", 30)
		.save();
		
		database.refreshSearchIndex();
		
		// Searching
		String query = "Doe";
		System.out.println("searching for '" + query + "'...");
		DocumentCollection col = database.search(query);
				
		// Printing the results
		System.out.println("Found " + col.size() + " persons.");
		
		for(Document doc : col) {
			//Map<String, Vector<Object>> fields = doc.getFields();
			Person p = (Person) doc;
			System.out.println("Name=" + p.getFieldAsString("Name"));
		}
		
		// Searching
		query = "John";
		System.out.println("searching for '" + query + "'...");
		col = database.search(query);
				
		// Printing the results
		System.out.println("Found " + col.size() + " persons.");
		
		for(Document doc : col) {
			Person p = (Person) doc;
			System.out.println("Name=" + p.getFieldAsString("Name"));
		}

		// Finding a John Doe by Id
		Document p = database.getDocument(Person.class, johnDoeId);
		if (p.isOpen()) {
			System.out.println("Found " + johnDoeId + ". His name is " + p.getFieldAsString("Name") + " and his age is " + p.getFieldAsInteger("Age"));
		} else {
			System.out.println("Not found " + johnDoeId + ".");
		}
		
		
		System.out.println("Done.");

		NotesThread.stermThread();
	}
}
