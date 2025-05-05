package server.commands;

import server.utils.Collection;
import shared.network.Request;
import shared.network.Response;

/** Command to print sum of minimal points of every element. */
public class SumOfMinimalPoint extends Command {
  private final Collection collection;

  public SumOfMinimalPoint(Collection collection) {
    super("sum_of_minimal_point");
    this.collection = collection;
  }

  @Override
  public Response execute(Request request) {
    return new Response(true, "Sum of the minimal points: " + collection.sumOfMinimalPoint());
  }
}
