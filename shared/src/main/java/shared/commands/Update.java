package shared.commands;

public class Update extends CommandData {
  private static final long serialVersionUID = 2L;

  public Update() {
    super("update", new String[] {"id"}, true, "Update element in the collection by its ID");
  }
}
