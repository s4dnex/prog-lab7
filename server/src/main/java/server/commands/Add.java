package server.commands;

import server.utils.Collection;
import shared.data.LabWork;
import shared.network.Response;

/** Command to build element and add it to collection. */
public class Add extends Command {
  private final Collection collection;

  public Add(Collection collection) {
    super("add");
    this.collection = collection;
  }

  @Override
  public Response execute(String[] args, Object obj) {
    if (obj == null || obj.getClass() != LabWork.class) {
      throw new IllegalArgumentException("Invalid object type. Expected LabWork.");
    }

    collection.add((LabWork) obj);

    return new Response(true, "Element has been added!");
  }
}
