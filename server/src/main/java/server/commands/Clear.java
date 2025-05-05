package server.commands;

import java.sql.SQLException;
import server.database.DatabaseManager;
import server.utils.Collection;
import server.utils.Logback;
import shared.network.Request;
import shared.network.Response;

/** Command to clear the collection. */
public class Clear extends Command {
  private final Collection collection;
  private final DatabaseManager databaseManager;

  public Clear(Collection collection, DatabaseManager databaseManager) {
    super("clear");
    this.collection = collection;
    this.databaseManager = databaseManager;
  }

  @Override
  public Response execute(Request request) {
    try {
      if (databaseManager.clear(request.getSession().getUsername())) {
        collection.clear(request.getSession().getUsername());
        return new Response(true, "Collection has been cleared!");
      }
      return new Response(false, "Unable to clear the collection.");
    } catch (SQLException e) {
      Logback.getLogger("Command").error(e.getMessage());
      return new Response(false, "Unable to clear the collection.");
    }
  }
}
