package local.test._local;

import local.mock.Base;
//import org.riverframework.no2.NotesThread;
import org.riverframework.River;
import org.riverframework.utils.Credentials;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Document;
import org.riverframework.wrapper.DocumentIterator;
import org.riverframework.wrapper.Session;

public class FactoryTest {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//NotesThread.sinitThread();

		Session<local.mock.Base> session = (Session<local.mock.Base>) River.getSession(River.LOTUS_DOMINO,
				(String) null, (String) null, Credentials.getPassword()).getWrapperObject();
		Database<local.mock.Base> database = (Database<Base>) session.getDatabase("", "massive.nsf");

		DocumentIterator<local.mock.Base, local.mock.Document> it = (DocumentIterator<Base, local.mock.Document>) database.getAllDocuments();
		int i = 0;
		
		while(it.hasNext()) {
			Document<local.mock.Document> doc = it.next();
			String counter = doc.getFieldAsString("counter");
			if (++i % 250 == 0) {
				System.out.println("=" + counter);
			}
			
			if (i > 5000) break;
		}
		
		System.out.println("Done");

		River.closeSession(River.LOTUS_DOMINO);

		//NotesThread.stermThread();
	}

}