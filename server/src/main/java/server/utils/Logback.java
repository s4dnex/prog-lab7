package server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logback {
  private Logback() {}

  public static Logger getLogger() {
    return LoggerFactory.getLogger("[Server]");
  }

  public static Logger getLogger(String name) {
    return LoggerFactory.getLogger("[Server/" + name + "]");
  }
}
