package server.utils;

import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import server.database.DatabaseManager;
import server.network.NetworkManager;
import shared.utils.Console;

public class ExitSaver implements Runnable {
  private final Collection collection;
  private final Console console;
  private final NetworkManager networkManager;
  private final DatabaseManager databaseManager;
  private final Logger logger = Logback.getLogger();

  public ExitSaver(
      Collection collection,
      Console console,
      NetworkManager networkManager,
      DatabaseManager databaseManager) {
    this.collection = collection;
    this.console = console;
    this.networkManager = networkManager;
    this.databaseManager = databaseManager;
  }

  @Override
  public void run() {
    logger.info("Starting server shutdown...");
    // try {
    //   logger.info("Saving collection to file...");
    // } catch (Exception e) {
    //   logger.error("Failed to save collection: " + e.getMessage());
    // }

    try {
      logger.info("Closing network manager...");
      networkManager.close();
    } catch (IOException e) {
      logger.error("Failed to close network manager: " + e.getMessage());
    }

    try {
      logger.info("Closing database connection...");
      databaseManager.close();
    } catch (SQLException e) {
      logger.error("Failed to close database connection: " + e.getMessage());
    }

    // try {
    //   logger.info("Closing console I/O...");
    //   console.close();
    // } catch (IOException e) {
    //   logger.error("Failed to close console I/O: " + e.getMessage());
    // }
  }
}
