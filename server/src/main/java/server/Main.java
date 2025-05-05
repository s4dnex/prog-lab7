package server;

import java.util.Arrays;
import java.util.Scanner;
import server.database.DatabaseConnector;
import server.database.DatabaseManager;
import server.database.QueryResultHandler;
import server.network.NetworkManager;
import server.utils.*;
import shared.io.*;
import shared.utils.*;

public class Main {
  public static void main(String[] args) {
    try (InputHandler inputHandler = new ScannerInput(new Scanner(System.in));
        OutputHandler outputHandler = new SystemOutput();
        Console console = new Console(inputHandler, outputHandler); ) {

      Collection collection = new Collection();
      DataBuilder dataBuilder = new DataBuilder(console);
      NetworkManager networkManager = new NetworkManager(dataBuilder.getServerPort());
      DatabaseManager databaseManager = new DatabaseManager(new DatabaseConnector().connect());
      PasswordManager passwordManager = new PasswordManager();
      Invoker invoker = new Invoker(collection, databaseManager, passwordManager);
      RequestHandler requestHandler = new RequestHandler(invoker, networkManager);
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(new ExitSaver(collection, console, networkManager, databaseManager)));

      Logback.getLogger().info("Retrieving collection from database...");
      QueryResultHandler.toLabWorks(databaseManager.getCollection())
          .forEach(
              (labWork) -> {
                collection.add(labWork);
              });

      Logback.getLogger().info("Server is ready for work.");
      requestHandler.enable();
    } catch (Exception e) {
      Logback.getLogger().error("Program error occurred (" + e.getClass() + "): " + e.getMessage());
      Logback.getLogger()
          .error(
              "Details of the error:\n"
                  + String.join(
                      "\n",
                      Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()));
      System.exit(1);
    }
  }
}
