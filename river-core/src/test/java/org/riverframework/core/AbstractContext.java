package org.riverframework.core;

import org.riverframework.RiverException;

public abstract class AbstractContext implements org.riverframework.Context {
    private String testDatabaseServer = "";
    private String testDatabasePath = "";

    public AbstractContext() {
        try {
            testDatabaseServer = "test-database-server";
            testDatabasePath = "test-database-path";

        } catch (Exception e) {
            throw new RiverException(e);
        }
    }

    @Override
    public String getTestDatabaseServer() {
        return testDatabaseServer;
    }

    @Override
    public String getTestDatabasePath() {
        return testDatabasePath;
    }

    @Override
    public abstract Session getSession();

    @Override
    public abstract String getConfigurationFileName();
}
