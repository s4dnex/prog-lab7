package shared.commands;

public class Login extends CommandData {
  private static final long serialVersionUID = 2L;

  public Login() {
    super("login", new String[0], false, "Log in to the system with given credentials", false);
  }
}
