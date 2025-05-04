package shared.commands;

public class Clear extends CommandData {
  private static final long serialVersionUID = 2L;

  public Clear() {
    super("clear", new String[0], false, "Clear the collection");
  }
}
