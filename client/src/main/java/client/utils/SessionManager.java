package client.utils;

import shared.network.Session;
import shared.utils.Console;
import shared.utils.DataBuilder;

public class SessionManager {
  private final Console console;
  private final DataBuilder dataBuilder;
  private Session session;

  public SessionManager(Console console, DataBuilder dataBuilder) {
    this.console = console;
    this.dataBuilder = dataBuilder;
  }

  /**
   * Method to initialize the session. It prompts the user for their username and password, and
   * creates a new session.
   *
   * @return true if the user has an account, false otherwise
   */
  public boolean initialize() {
    boolean hasAccount = false;

    while (true) {
      console.print("Do you already have an account? [y/n] (default: no): ");
      String answer = console.readln();
      if (answer == null || answer.isBlank() || answer.toLowerCase().equals("n")) {
        hasAccount = false;
        break;
      } else if (answer.toLowerCase().equals("y")) {
        hasAccount = true;
        break;
      } else {
        console.print("Please enter 'y' or 'n'.\n");
      }
    }

    return hasAccount;
  }

  public Session createSession() {
    String username = dataBuilder.getUsername();
    String password = dataBuilder.getPassword();
    session = new Session(username, password);
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

  public Session getSession() {
    return session;
  }
}
