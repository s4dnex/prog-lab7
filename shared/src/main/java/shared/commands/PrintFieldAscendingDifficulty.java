package shared.commands;

public class PrintFieldAscendingDifficulty extends CommandData {
  private static final long serialVersionUID = 2L;

  public PrintFieldAscendingDifficulty() {
    super(
        "print_field_ascending_difficulty",
        new String[0],
        false,
        "Print the 'difficulty' field of all elements in ascending order");
  }
}
