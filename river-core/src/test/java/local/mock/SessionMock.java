package local.mock;

import org.dizitart.no2.Nitrite;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMock extends BaseMock {
    Map<String, DatabaseMock> databases = new ConcurrentHashMap<String, DatabaseMock>();

    public DatabaseMock createDatabase(Object... parameters) {
        String databaseName = ((String) parameters[0]).trim();

        if (databaseName.length() == 0) {
            throw new MockException("Database's name must not be empty. ");
        }

        if (databases.get(databaseName) != null) {
            throw new MockException("Database exists with the name " + databaseName + ".");
        }

        DatabaseMock database = new DatabaseMock(this, Nitrite.builder().openOrCreate(), databaseName);
        databases.put(databaseName, database);

        return database;
    }

    public DatabaseMock getDatabase(String... parameters) {
        String databaseName = parameters[0].trim();

        return databases.get(databaseName);
    }

    public void removeDatabase(String... parameters) {
        String databaseName = parameters[0].trim();

        DatabaseMock database = databases.get(databaseName);

        if (database == null) {
            throw new MockException("Database does not exist with the name " + databaseName + ".");
        }

        database.close();
        databases.remove(databaseName);
    }

    public String getServerName() {
        return "localhost";
    }

    public String getUserName() {
        return System.getProperty("user.name");
    }

}
