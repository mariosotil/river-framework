package local.tests._local;

import local.mock.BaseMock;
//import org.riverframework.no2.NotesThread;
import local.mock.DocumentMock;
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

		Session<BaseMock> session = (Session<BaseMock>) River.getSession(River.LOTUS_DOMINO,
				(String) null, (String) null, Credentials.getPassword()).getWrapperObject();
		Database<BaseMock> database = (Database<BaseMock>) session.getDatabase("", "massive.nsf");

		DocumentIterator<BaseMock, DocumentMock> it = (DocumentIterator<BaseMock, DocumentMock>) database.getAllDocuments();
		int i = 0;
		
		while(it.hasNext()) {
			Document<DocumentMock> doc = it.next();
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