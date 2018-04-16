package org.riverframework.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

public class LoggerHelperTest {

  @Test
  public void testFileHandler() {
    Logger log = Logger.getLogger(this.getClass().getName());

    new LoggerHelper(log)
        .setUseParentHandlers(false)
        .clearHandlers()
        .addFileHandler("d:\\test-logger-{{yyyyMMddHHmmss}}.txt")
        .setLevel(Level.ALL);

    log.info("TEST1");
    log.info("TEST2");
    log.info("TEST3");

    //TODO: finish the unit test
  }
}
