package shared.commands;

public class Show extends CommandData {
  private static final long serialVersionUID = 2L;

  public Show() {
    super("show", new String[0], false, "Show all elements in the collection");
  }
}
