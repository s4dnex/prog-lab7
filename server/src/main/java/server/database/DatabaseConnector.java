package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import server.utils.Logback;

public class DatabaseConnector {
  private static final Logger logger = Logback.getLogger("Database");

  public Connection connect() throws SQLException {
    logger.info("Connecting to PostgreSQL database...");
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new SQLException("PostgreSQL JDBC Driver not found.", e);
    }

    String login = System.getenv("DB_LOGIN");
    String password = System.getenv("DB_PASSWORD");
    String host = System.getenv("DB_HOST");
    String port = System.getenv("DB_PORT");
    String database = System.getenv("DB_DATABASE");

    if (login == null || password == null || host == null || port == null || database == null) {
      throw new SQLException("Database credentials are not set.");
    }

    return DriverManager.getConnection(
        "jdbc:postgresql://" + host + ":" + port + "/" + database + "?charSet=UTF-8",
        login,
        password);
  }
}
