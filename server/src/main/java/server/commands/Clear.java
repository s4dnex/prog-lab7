package server.commands;

import server.utils.Collection;
import shared.network.Response;

/** Command to clear the collection. */
public class Clear extends Command {
  private final Collection collection;

  public Clear(Collection collection) {
    super("clear");
    this.collection = collection;
  }

  @Override
  public Response execute(String[] args, Object obj) {
    collection.clear();
    return new Response(true, "Collection has been cleared!");
  }
}
