package org.riverframework;

import org.riverframework.core.Session;

public interface Context {

  public String getTestDatabaseServer();

  public String getTestDatabasePath();

  public String getConfigurationFileName();

  public Session getSession();

  public void closeSession();
}
