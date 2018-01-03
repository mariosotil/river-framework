package local.wrapper;

import local.mock.BaseMock;
import local.mock.MockException;
import local.mock.DatabaseMock;
import local.mock.SessionMock;
import org.riverframework.River;
import org.riverframework.RiverException;
import org.riverframework.wrapper.Database;
import org.riverframework.wrapper.Factory;
import org.riverframework.wrapper.Session;

import java.util.Arrays;
import java.util.logging.Logger;

public class DefaultSession extends AbstractBaseWrapper<SessionMock> implements Session<SessionMock> {
    private static final Logger log = River.LOG_WRAPPER_LOTUS_DOMINO;

    protected DefaultSession(Session<SessionMock> dummy, SessionMock __native) {
        super(dummy, __native);
        _factory = DefaultFactory.getInstance();
    }

    @Override
    public String calcObjectId(SessionMock __session) {
        String objectId = "";

        if (__session != null) {
            try {

                StringBuilder sb = new StringBuilder(1024);
                sb.append(__session.getServerName());
                sb.append(River.ID_SEPARATOR);
                sb.append(__session.getUserName());
                sb.append(River.ID_SEPARATOR);
                sb.append(__session.hashCode());

                objectId = sb.toString();
            } catch (MockException e) {
                throw new RiverException(e);
            }
        }

        return objectId;
    }

    public Factory<BaseMock> getFactory() {
        return _factory;
    }

    @Override
    public boolean isOpen() {
        return (__native != null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Database<DatabaseMock> createDatabase(String... location) {
        log.fine("location=" + Arrays.deepToString(location));

        Database<DatabaseMock> _database;
        DatabaseMock __database;

        _database = getDatabase(location);

        if (!_database.isOpen()) {
            __database = __native.createDatabase(location[0]);

            _database = (Database<DatabaseMock>) getFactory().getDatabase(__database);

        } else {
            _database = (Database<DatabaseMock>) getFactory().getDatabase(null);
        }

        return _database;
    }

    @Override
    public Database<DatabaseMock> getDatabase(String... parameters) {
        log.fine("location=" + Arrays.deepToString(parameters));

        DatabaseMock __database = null;

        if (parameters.length != 1) throw new RiverException("It was expected one parameter: database's name");

        __database = __native.getDatabase(parameters);

        @SuppressWarnings("unchecked")
        Database<DatabaseMock> database = (Database<DatabaseMock>) getFactory().getDatabase(__database);
        return database;
    }

    @Override
    public String getUserName() {
        String userName;

        try {
            userName = __native.getUserName();
        } catch (MockException e) {
            throw new RiverException(e);
        }

        log.finest("getUserName=" + userName);
        return userName;
    }

    @Override
    public void close() {
        log.fine("Closing factory");
        getFactory().close();

        log.info("Session closed.");
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + objectId + ")";
    }
}
