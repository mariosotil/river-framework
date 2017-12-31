package local.mock;

import java.util.Date;

public class Session extends Base {
    public Database getDatabase(Object... parameters) {
        return null;
    }

    public DateTime createDateTime(Date date)  {
        return null;
    }

    public String getServerName() { return null; }

    public String getUserName() { return null; }

}
