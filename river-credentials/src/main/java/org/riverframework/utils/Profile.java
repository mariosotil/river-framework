package org.riverframework.utils;

import lombok.Getter;
import lombok.Setter;

public class Profile {
  @Getter @Setter
  private String server;

  @Getter @Setter
  private String username;

  @Getter @Setter
  private String password;
}
