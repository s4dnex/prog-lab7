package shared.commands;

public class History extends CommandData {
  private static final long serialVersionUID = 2L;

  public History() {
    super("history", new String[0], false, "Display last 15 executed commands");
  }
}
