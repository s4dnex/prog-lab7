package shared.commands;

public class RemoveLower extends CommandData {
  private static final long serialVersionUID = 2L;

  public RemoveLower() {
    super(
        "remove_lower",
        new String[0],
        true,
        "Remove all elements from the collection that are less than the specified one");
  }
}
