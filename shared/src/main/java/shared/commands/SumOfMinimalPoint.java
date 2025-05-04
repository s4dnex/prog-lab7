package shared.commands;

public class SumOfMinimalPoint extends CommandData {
  private static final long serialVersionUID = 2L;

  public SumOfMinimalPoint() {
    super(
        "sum_of_minimal_point",
        new String[0],
        false,
        "Print the sum of the minimal point of all elements in the collection");
  }
}
