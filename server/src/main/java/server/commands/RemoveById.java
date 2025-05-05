package server.commands;

import java.sql.SQLException;
import server.database.DatabaseManager;
import server.database.QueryResultHandler;
import server.utils.Collection;
import server.utils.Logback;
import shared.network.Request;
import shared.network.Response;

/** Command to remove element from collection by its id. */
public class RemoveById extends Command {
  private final Collection collection;
  private final DatabaseManager databaseManager;

  public RemoveById(Collection collection, DatabaseManager databaseManager) {
    super("remove_by_id");
    this.collection = collection;
    this.databaseManager = databaseManager;
  }

  @Override
  public Response execute(Request request) {
    long id;
    String[] args = request.getArgs();

    try {
      id = Long.parseLong(args[0]);
    } catch (NumberFormatException e) {
      return new Response(false, "ID must be a long integer");
    }

    try {
      if (!databaseManager.checkIfLabworkExists(id)) {
        return new Response(false, "Labwork with ID " + id + " does not exist!");
      }

      if (!QueryResultHandler.toLabworkOwner(databaseManager.getLabworkById(id))
          .equals(request.getSession().getUsername())) {
        return new Response(false, "You are not the owner of this element!");
      }

      if (databaseManager.deleteLabworkById(id)) {
        collection.remove(id);
        return new Response(true, "Labwork with ID " + id + " has been removed!");
      } else {
        return new Response(false, "Unable to remove element from the database.");
      }
    } catch (SQLException e) {
      Logback.getLogger("Command").error(e.getMessage());
      return new Response(false, "Unable to remove element from the database because of an error.");
    }
  }
}
