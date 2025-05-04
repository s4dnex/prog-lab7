package shared.commands;

public class RemoveById extends CommandData {
  private static final long serialVersionUID = 2L;

  public RemoveById() {
    super(
        "remove_by_id", new String[] {"id"}, false, "Remove element from the collection by its ID");
  }
}
