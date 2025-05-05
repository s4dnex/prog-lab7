package server.commands;

import java.sql.SQLException;
import server.database.DatabaseManager;
import server.database.QueryResultHandler;
import server.utils.Collection;
import server.utils.Logback;
import shared.data.Labwork;
import shared.network.Request;
import shared.network.Response;

/** Command to update element in collection by its id. */
public class Update extends Command {
  private final Collection collection;
  private final DatabaseManager databaseManager;

  public Update(Collection collection, DatabaseManager databaseManager) {
    super("update");
    this.collection = collection;
    this.databaseManager = databaseManager;
  }

  @Override
  public Response execute(Request request) {
    long id;
    Object obj = request.getCommandObject();
    String[] args = request.getArgs();

    if (obj == null || obj.getClass() != Labwork.class) {
      throw new IllegalArgumentException("Invalid object type. Expected LabWork.");
    }

    try {
      id = Long.parseLong(args[0]);
    } catch (NumberFormatException e) {
      return new Response(false, "ID must be a long integer");
    }

    Labwork labwork = (Labwork) obj;

    try {
      if (!databaseManager.checkIfLabworkExists(id)) {
        return new Response(false, "Labwork with ID " + id + " does not exist!");
      }

      if (!QueryResultHandler.toLabworkOwner(databaseManager.getLabworkById(id))
          .equals(request.getSession().getUsername())) {
        return new Response(false, "You are not the owner of this element!");
      }

      if (databaseManager.updateLabwork(id, labwork)) {
        collection.update(id, labwork);
        return new Response(true, "Labwork with ID " + id + " has been updated!");
      } else {
        return new Response(false, "Unable to update the element.");
      }
    } catch (SQLException e) {
      Logback.getLogger("Command").error(e.getMessage());
      return new Response(false, "Unable to check and update element in the database.");
    }
  }
}
