package server.commands;

import server.utils.Collection;
import shared.network.Response;

/** Command to print element's authors in descending order. */
public class PrintFieldDescendingAuthor extends Command {
  private final Collection collection;

  public PrintFieldDescendingAuthor(Collection collection) {
    super("print_field_descending_author");
    this.collection = collection;
  }

  @Override
  public Response execute(String[] args, Object obj) {
    var fields = collection.getDescendingAuthor();

    if (fields.isEmpty()) {
      return new Response(true, "There are no authors in collection, yet.");
    }

    StringBuilder msg = new StringBuilder("Authors in descending order:\n");
    long i = 1;
    for (var field : fields) {
      msg.append((i++) + ". " + field + "\n");
    }

    return new Response(true, msg.toString());
  }
}
