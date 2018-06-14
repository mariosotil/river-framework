package org.riverframework.utils;

import static junit.framework.TestCase.assertTrue;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CredentialsTest {
  static final String SERVER_PARAMETER = "SERVER_AAA";
  static final String USERNAME_PARAMETER = "USERNAME_AAA";
  static final String PASSWORD_PARAMETER = "PASSWORD_AAA";

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  public File initializeConfigFile() {
    try {
      folder.newFolder(".river-framework");
      File configFile = folder.newFile(".river-framework/credentials");

      List<String> lines = new ArrayList<>();
      lines.add("server: " + SERVER_PARAMETER);
      lines.add("username: " + USERNAME_PARAMETER);
      lines.add("password: " + PASSWORD_PARAMETER);

      Files.asCharSink(configFile, StandardCharsets.UTF_8).writeLines(lines);

      return configFile;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testLoadCredentials() {
    File configFile = initializeConfigFile();

    Credentials credentials = new Credentials(configFile);

    String server = credentials.getServer();
    String username = credentials.getUsername();
    String password = credentials.getPassword();

    assertTrue("The server parameter could't be read from configuration file", server.equals(SERVER_PARAMETER));
    assertTrue("The username parameter could't be read from configuration file", username.equals(USERNAME_PARAMETER));
    assertTrue("The password parameter could't be read from configuration file", password.equals(PASSWORD_PARAMETER));
  }

}
