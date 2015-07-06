package org.riverframework.wrapper.org.openntf.domino._local;

import lotus.domino.NotesThread;

import org.openntf.domino.Base;
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

		Session<org.openntf.domino.Base<?>> session = (Session<org.openntf.domino.Base<?>>) River.getSession(River.ORG_OPENNTF_DOMINO,
				(String) null, (String) null, Credentials.getPassword()).getWrapperObject();
		
		Database<org.openntf.domino.Database> database = (Database<org.openntf.domino.Database>) session.getDatabase("", "massive.nsf");

		DocumentIterator<org.openntf.domino.Base<?>,org.openntf.domino.Document> it = (DocumentIterator<Base<?>, org.openntf.domino.Document>) database.getAllDocuments();
		int i = 0;
		
		while(it.hasNext()) {
			Document<org.openntf.domino.Document> doc = it.next();
			String counter = doc.getFieldAsString("counter");
			if (++i % 250 == 0) {
				System.out.println("=" + counter);
			}
			
			if (i > 5000) break;
		}
		
		System.out.println("Done");

		River.closeSession(River.ORG_OPENNTF_DOMINO);
		
		NotesThread.stermThread();
	}

}