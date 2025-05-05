package server.commands;

import java.sql.SQLException;
import server.database.DatabaseManager;
import server.database.QueryResultHandler;
import server.utils.Collection;
import server.utils.Logback;
import shared.data.Labwork;
import shared.network.Request;
import shared.network.Response;

/** Command to build element and add it to collection if it is greater than any other. */
public class AddIfMax extends Command {
  private final Collection collection;
  private final DatabaseManager databaseManager;

  public AddIfMax(Collection collection, DatabaseManager databaseManager) {
    super("add_if_max");
    this.collection = collection;
    this.databaseManager = databaseManager;
  }

  @Override
  public Response execute(Request request) {
    Object obj = request.getCommandObject();

    if (obj.getClass() != Labwork.class) {
      return new Response(false, "Invalid object type. Expected LabWork.");
    }

    Labwork labwork = (Labwork) obj;

    try {
      Long maxLabWork = QueryResultHandler.toLong(databaseManager.getMaxLabwork());
      if (labwork.getMinimalPoint() == null || labwork.getMinimalPoint() <= maxLabWork) {
        return new Response(
            true,
            "Element has NOT been added to collection. It is not greater than any other element.");
      }

      if (databaseManager.addLabwork(labwork)) {
        collection.add(labwork);
        return new Response(true, "Element has been added to collection!");
      } else {
        return new Response(false, "Unable to add element to the database.");
      }
    } catch (SQLException e) {
      Logback.getLogger("Command").error(e.getMessage());
      return new Response(false, "Unable to compare and add element to the database.");
    }
  }
}
