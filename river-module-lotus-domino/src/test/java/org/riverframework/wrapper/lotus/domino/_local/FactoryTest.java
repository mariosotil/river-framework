package org.riverframework.wrapper.lotus.domino._local;

import lotus.domino.NotesThread;

import org.riverframework.River;
import org.riverframework.core.Credentials;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;

public class FactoryTest {
	public static void main(String[] args) {
		NotesThread.sinitThread();

		Session session = (Session) River.getSession(River.LOTUS_DOMINO,
				(String) null, (String) null, Credentials.getPassword()).getWrapperObject();
		Database database = session.getDatabase("", "massive.nsf");

		DocumentIterator it = database.getAllDocuments();
		int i = 0;
		
		while(it.hasNext()) {
			Document doc = it.next();
			String counter = doc.getFieldAsString("counter");
			if (++i % 250 == 0) {
				System.out.println("=" + counter);
			}
			
			if (i > 5000) break;
		}
		
		System.out.println("Done");

		River.closeSession(River.LOTUS_DOMINO);
		
		NotesThread.stermThread();
	}

}