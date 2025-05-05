package shared.commands;

public class Register extends CommandData {
  private static final long serialVersionUID = 2L;

  public Register() {
    super("register", new String[0], false, "Register a new user in the system", false);
  }
}
