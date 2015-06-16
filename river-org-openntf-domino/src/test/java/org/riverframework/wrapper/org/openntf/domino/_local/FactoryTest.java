package org.riverframework.wrapper.org.openntf.domino._local;

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

		@SuppressWarnings("unchecked")
		Session<org.openntf.domino.Base> session = (Session<org.openntf.domino.Base>) River.getSession(River.LOTUS_DOMINO,
				(String) null, (String) null, Credentials.getPassword()).getWrapperObject();
		Database<org.openntf.domino.Base> database = session.getDatabase("", "massive.nsf");

		DocumentIterator<org.openntf.domino.Base> it = database.getAllDocuments();
		int i = 0;
		
		while(it.hasNext()) {
			Document<org.openntf.domino.Base> doc = it.next();
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