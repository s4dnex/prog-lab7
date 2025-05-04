package server.commands;

import java.util.TreeSet;
import server.utils.Collection;
import shared.data.LabWork;
import shared.network.Response;

/** Command to show collection elements. */
public class Show extends Command {
  private final Collection collection;

  public Show(Collection collection) {
    super("show");
    this.collection = collection;
  }

  @Override
  public Response execute(String[] args, Object obj) {
    TreeSet<LabWork> labWorks = collection.asTreeSet();
    if (labWorks.size() == 0) {
      return new Response(true, "Collection is empty!");
    }

    long i = 1;
    StringBuilder msg =
        new StringBuilder("The collection contains " + labWorks.size() + " element(s):\n");
    for (LabWork lw : labWorks) {
      msg.append((i++) + ". " + lw + "\n");
    }
    return new Response(true, msg.toString());
  }
}
