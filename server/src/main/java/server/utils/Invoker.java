package server.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import server.commands.*;
import server.database.DatabaseManager;
import shared.network.Request;
import shared.network.Response;

/** Class to register commands and invoke their execution. */
public class Invoker {
  private final DatabaseManager databaseManager;
  private final PasswordManager passwordManager;
  private final ConcurrentLinkedQueue<Command> executedCommands;
  private final ConcurrentHashMap<String, Command> commands;
  private final Logger logger = Logback.getLogger("Invoker");

  /**
   * @param console Class to handle input and output
   * @param fileHandler Class to handle files
   * @param collection Class to store and work with data
   */
  public Invoker(
      Collection collection, DatabaseManager databaseManager, PasswordManager passwordManager) {
    this.databaseManager = databaseManager;
    this.passwordManager = passwordManager;
    executedCommands = new ConcurrentLinkedQueue<Command>();
    commands = new ConcurrentHashMap<String, Command>();

    commands.put(new shared.commands.Add().getName(), new Add(collection, databaseManager));
    commands.put(
        new shared.commands.AddIfMax().getName(), new AddIfMax(collection, databaseManager));
    commands.put(new shared.commands.Clear().getName(), new Clear(collection, databaseManager));
    commands.put(new shared.commands.Help().getName(), new Help());
    commands.put(new shared.commands.History().getName(), new History(executedCommands));
    commands.put(new shared.commands.Info().getName(), new Info(collection));
    commands.put(
        new shared.commands.Login().getName(), new Login(databaseManager, passwordManager));
    commands.put(
        new shared.commands.PrintFieldAscendingDifficulty().getName(),
        new PrintFieldAscendingDifficulty(collection));
    commands.put(
        new shared.commands.PrintFieldDescendingAuthor().getName(),
        new PrintFieldDescendingAuthor(collection));
    commands.put(
        new shared.commands.Register().getName(), new Register(databaseManager, passwordManager));
    commands.put(
        new shared.commands.RemoveById().getName(), new RemoveById(collection, databaseManager));
    commands.put(new shared.commands.Show().getName(), new Show(collection));
    commands.put(
        new shared.commands.SumOfMinimalPoint().getName(), new SumOfMinimalPoint(collection));
    commands.put(new shared.commands.Test().getName(), new Test());
    commands.put(new shared.commands.Update().getName(), new Update(collection, databaseManager));
  }

  /**
   * Method that parses command from String and executes it
   *
   * @param command Command to execute
   */
  public Response execute(Request request) {
    String commandName = request.getCommandData().getName();

    if (request.getCommandData().requiresAuth()) {
      Response response = commands.get("login").execute(request);
      if (!response.isSuccess()) {
        return new Response(false, "Unable to authenticate, please, try to login again.");
      }
    }

    if (commands.containsKey(commandName)) {
      logger.info("Executing command '" + commandName + "'");
      try {
        Response response = commands.get(commandName).execute(request);

        if (executedCommands.size() > 15) executedCommands.poll();

        executedCommands.add(commands.get(commandName));
        return response;
      } catch (IllegalArgumentException e) {
        return new Response(false, e.getMessage());
      }
    } else {
      logger.error("Command '" + commandName + "' not found.");
      return new Response(false, "Command not found. Type 'help' to see available commands.");
    }
  }
}
