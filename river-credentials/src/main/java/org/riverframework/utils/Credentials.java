package org.riverframework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;

/**
 * <p>Allows creates a Session using the credentials stored in the file system. The file must be
 * named as
 * $user_home\.river-framework\credentials and it must have this content in YAML format:</p>
 *
 * <code>
 * server: SERVERNAME
 * username: USERNAME
 * password: PASSWORD
 * </code>
 *
 * @author mario.sotil@gmail.com
 */
@Singleton
public final class Credentials {

  private Profile current;

  public Credentials() {
    String location = System.getProperty("user.home")
            + File.separator
            + ".river-framework"
            + File.separator
            + "credentials";
    initialize(new File(location));
  }

  public Credentials(File file) {
    initialize(file);
  }

  private void initialize(File file) {

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      current = mapper.readValue(file, Profile.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the server defined in the credentials file.
   *
   * @return server's name
   */
  public String getServer() {
    return current.getServer();
  }

  /**
   * Returns the user name defined in the credentials file.
   *
   * @return username
   */
  public String getUsername() {
    return current.getUsername();
  }

  /**
   * Returns the password defined in the credentials file.
   *
   * @return password
   */
  public String getPassword() {
    return current.getPassword();
  }
}
