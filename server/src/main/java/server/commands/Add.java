package server.commands;

import java.sql.SQLException;
import server.database.DatabaseManager;
import server.utils.Collection;
import server.utils.Logback;
import shared.data.Labwork;
import shared.network.Request;
import shared.network.Response;

/** Command to build element and add it to collection. */
public class Add extends Command {
  private final Collection collection;
  private final DatabaseManager databaseManager;

  public Add(Collection collection, DatabaseManager databaseManager) {
    super("add");
    this.collection = collection;
    this.databaseManager = databaseManager;
  }

  @Override
  public Response execute(Request request) {
    Object obj = request.getCommandObject();

    if (obj == null || obj.getClass() != Labwork.class) {
      throw new IllegalArgumentException("Invalid object type. Expected LabWork.");
    }

    Labwork labwork = (Labwork) obj;

    try {
      if (databaseManager.addLabwork(labwork)) {
        collection.add(labwork);
        return new Response(true, "Element has been added!");
      } else {
        return new Response(false, "Unable to add element to the database.");
      }
    } catch (SQLException e) {
      Logback.getLogger("Command").error(e.getMessage());
      return new Response(false, "Unable to add element to the database because of an error.");
    }
  }
}
