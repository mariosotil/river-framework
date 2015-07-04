package org.riverframework.wrapper.lotus.domino._local;

import lotus.domino.Base;
import lotus.domino.NotesThread;

import org.riverframework.River;
import org.riverframework.utils.Credentials;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;

public class FactoryTest {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		NotesThread.sinitThread();

		Session<lotus.domino.Base> session = (Session<lotus.domino.Base>) River.getSession(River.LOTUS_DOMINO,
				(String) null, (String) null, Credentials.getPassword()).getWrapperObject();
		Database<lotus.domino.Base> database = (Database<Base>) session.getDatabase("", "massive.nsf");

		DocumentIterator<lotus.domino.Base, lotus.domino.Document> it = (DocumentIterator<Base, lotus.domino.Document>) database.getAllDocuments();
		int i = 0;
		
		while(it.hasNext()) {
			Document<lotus.domino.Document> doc = it.next();
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