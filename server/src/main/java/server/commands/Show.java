package server.commands;

import java.util.List;
import server.utils.Collection;
import shared.data.Labwork;
import shared.network.Request;
import shared.network.Response;

/** Command to show collection elements. */
public class Show extends Command {
  private final Collection collection;

  public Show(Collection collection) {
    super("show");
    this.collection = collection;
  }

  @Override
  public Response execute(Request request) {
    List<Labwork> labWorks = collection.asList();
    if (labWorks.size() == 0) {
      return new Response(true, "Collection is empty!");
    }

    long i = 1;
    StringBuilder msg =
        new StringBuilder("The collection contains " + labWorks.size() + " element(s):\n");
    for (Labwork lw : labWorks) {
      msg.append((i++) + ". " + lw + "\n");
    }
    return new Response(true, msg.toString());
  }
}
