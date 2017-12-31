package local.test._local;

//import org.riverframework.no2.NotesThread;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class SessionTest extends org.riverframework.wrapper.AbstractSessionTest {
	@BeforeClass
	public static void before() {
		//NotesThread.sinitThread();
	}

	@AfterClass
	public static void after() {
		//NotesThread.stermThread();
	}

}
