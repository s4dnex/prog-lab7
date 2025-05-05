package shared.commands;

public class Test extends CommandData {
  private static final long serialVersionUID = 2L;

  public Test() {
    super("test", new String[0], false, "Test connection between client and server", false);
  }
}
