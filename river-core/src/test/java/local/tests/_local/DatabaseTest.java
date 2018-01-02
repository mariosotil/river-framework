package local.tests._local;

//import org.riverframework.no2.NotesThread;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DatabaseTest extends org.riverframework.wrapper.AbstractDatabaseTest {
	@BeforeClass
	public static void before() {
		//NotesThread.sinitThread();
	}

	@AfterClass
	public static void after() {
		//NotesThread.stermThread();
	}
}
