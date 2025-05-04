package shared.commands;

public class AddIfMax extends CommandData {
  private static final long serialVersionUID = 2L;

  public AddIfMax() {
    super(
        "add_if_max",
        new String[0],
        true,
        "Add a new element to the collection if it is greater than any other");
  }
}
