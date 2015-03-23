package org.riverframework.domino.demo;

import javax.sound.midi.Synthesizer;

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

	public static void main(String[] args) {
		NotesThread.sinitThread();

		Session session = DefaultSession.getInstance().open(Credentials.getPassword());
		Database database = session.getDatabase(DefaultDatabase.class, "", filepath);

		System.out.println("User=" + session.getUserName());
		System.out.println("Database=" + database.getName());

		// Deleting everything
		database.getAllDocuments().removeAll();

		// Creating three persons
		database.createDocument(Person.class)
		.setField("Name", "John Doe")
		.save();

		database.createDocument(Person.class)
		.setField("Name", "Jane Doe")
		.save();

		database.createDocument(Person.class)
		.setField("Name", "John Smith")
		.save();
		
		// Searching
		String query = "Doe";
		System.out.println("searching for '" + query + "'...");
		DocumentCollection col = database.search(query);
				
		// Printing the results
		System.out.println("Found " + col.size() + " persons.");
		
		while(col.hasNext()) {
			Person p = (Person) col.next();
			System.out.println("Name=" + p.getFieldAsString("Name"));
		}
		
		// Searching
		query = "John";
		System.out.println("searching for '" + query + "'...");
		col = database.search(query);
				
		// Printing the results
		System.out.println("Found " + col.size() + " persons.");
		
		while(col.hasNext()) {
			Person p = (Person) col.next();
			System.out.println("Name=" + p.getFieldAsString("Name"));
		}
				
		System.out.println("Done.");

		NotesThread.stermThread();
	}
}
