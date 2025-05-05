package server.commands;

import java.sql.SQLException;
import server.database.DatabaseManager;
import server.utils.Logback;
import server.utils.PasswordManager;
import shared.network.Request;
import shared.network.Response;

public class Register extends Command {
  private final DatabaseManager databaseManager;
  private final PasswordManager passwordManager;

  public Register(DatabaseManager databaseManager, PasswordManager passwordManager) {
    super("register");
    this.databaseManager = databaseManager;
    this.passwordManager = passwordManager;
  }

  @Override
  public Response execute(Request request) {
    try {
      String username = request.getSession().getUsername();
      if (databaseManager.checkIfUserExists(username)) {
        return new Response(false, "User already exists.");
      }

      String password = request.getSession().getPassword();
      String salt = passwordManager.generateSalt();
      String hashPassword = passwordManager.hashPassword(password, salt);
      if (databaseManager.addUser(username, hashPassword, salt)) {
        return new Response(true, "Registration is successful!");
      } else {
        return new Response(false, "Unable to register a new user, please, try again.");
      }
    } catch (SQLException e) {
      Logback.getLogger("Command").error(e.getMessage());
      return new Response(false, "Unexpected database error, please, try again.");
    }
  }
}
