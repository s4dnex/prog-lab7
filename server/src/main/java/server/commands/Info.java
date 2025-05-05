package server.commands;

import server.utils.Collection;
import shared.network.Request;
import shared.network.Response;

/** Command to show info about the collection. */
public class Info extends Command {
  private final Collection collection;

  public Info(Collection collection) {
    super("info");
    this.collection = collection;
  }

  @Override
  public Response execute(Request request) {
    StringBuilder msg = new StringBuilder();
    msg.append("Type: " + collection.getType() + "\n");
    msg.append("Size: " + collection.getSize() + "\n");
    return new Response(true, msg.toString());
  }
}
