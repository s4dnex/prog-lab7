package server.commands;

import java.sql.SQLException;
import server.database.DatabaseManager;
import server.database.QueryResultHandler;
import server.utils.Logback;
import server.utils.PasswordManager;
import shared.network.Request;
import shared.network.Response;

public class Login extends Command {
  private final DatabaseManager databaseManager;
  private final PasswordManager passwordManager;

  public Login(DatabaseManager databaseManager, PasswordManager passwordManager) {
    super("login");
    this.databaseManager = databaseManager;
    this.passwordManager = passwordManager;
  }

  @Override
  public Response execute(Request request) {
    try {
      String username = request.getSession().getUsername();
      String password = request.getSession().getPassword();
      String salt = QueryResultHandler.toString(databaseManager.getSalt(username));
      String hashPassword = passwordManager.hashPassword(password, salt);
      if (databaseManager.authenticateUser(username, hashPassword)) {
        return new Response(true, "Login successful!");
      } else {
        return new Response(false, "Invalid username or password.");
      }
    } catch (SQLException e) {
      Logback.getLogger("Command").error(e.getMessage());
      return new Response(false, "Unexpected database error, please, try again.");
    }
  }
}
