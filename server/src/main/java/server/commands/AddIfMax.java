package server.commands;

import server.utils.Collection;
import shared.data.LabWork;
import shared.network.Response;

/** Command to build element and add it to collection if it is greater than any other. */
public class AddIfMax extends Command {
  private final Collection collection;

  public AddIfMax(Collection collection) {
    super("add_if_max");
    this.collection = collection;
  }

  @Override
  public Response execute(String[] args, Object obj) {
    if (obj.getClass() != LabWork.class) {
      return new Response(false, "Invalid object type. Expected LabWork.");
    }

    if (collection.addIfMax((LabWork) obj)) {
      return new Response(true, "Element has been added to collection!");
    } else {
      return new Response(
          false,
          "Element has NOT been added to collection. It is not greater than any other element.");
    }
  }
}
