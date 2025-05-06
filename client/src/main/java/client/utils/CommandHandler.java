package client.utils;

import client.commands.*;
import client.network.NetworkManager;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import shared.commands.CommandData;
import shared.commands.CommandDataManager;
import shared.data.Labwork;
import shared.network.*;
import shared.utils.Console;
import shared.utils.DataBuilder;

/** Class to register commands and invoke their execution. */
public class CommandHandler {
  private final Console console;
  private final SessionManager sessionManager;
  private final DataBuilder dataBuilder;
  private final Map<String, Command> commands;
  private final NetworkManager networkManager;

  /**
   * @param console Class to handle input and output
   * @param dataBuilder Class to build data
   * @param networkManager Class to handle network connection
   */
  public CommandHandler(
      Console console,
      SessionManager sessionManager,
      DataBuilder dataBuilder,
      NetworkManager networkManager) {
    this.console = console;
    this.sessionManager = sessionManager;
    this.dataBuilder = dataBuilder;
    this.networkManager = networkManager;
    this.commands = new HashMap<>();

    register(new ExecuteScript(console, this));
    register(new Exit(console));
    register(new Help(console, commands));
  }

  /**
   * Method to register command to allow further execution
   *
   * @param command Command to register
   */
  private void register(Command command) {
    commands.put(command.getName(), command);
  }

  /**
   * Method that parses command, checks if it is valid and sends it to the server.
   *
   * @param command Command to execute
   */
  public void handle(String command) throws IOException {
    SimpleEntry<String, String[]> commandParts = CommandReader.parse(command);
    String commandName = commandParts.getKey();
    String[] args = commandParts.getValue();

    if (!CommandDataManager.has(commandName) && !commands.containsKey(commandName)) {
      String msg = "Command not found. Type 'help' to see available commands.";

      if (console.isInteractiveMode()) console.println(msg);
      else throw new RuntimeException(msg);
      return;
    }

    if (commands.containsKey(commandName)) {
      var cmnd = commands.get(commandName);
      if (args.length != cmnd.getArgs().length) {
        console.println("Unexpected arguments occurred, please, try again.");
        return;
      }

      cmnd.execute(args);
    }

    if (!CommandDataManager.has(commandName)) {
      return;
    }

    CommandData commandData = CommandDataManager.get(commandName);

    if (args.length != commandData.getArgs().length) {
      console.println("Unexpected arguments occurred, please, try again.");
      return;
    }

    Request request = createRequest(commandData, args);
    Response response = sendAndReceive(request);

    if (response.isSuccess()) {
      console.println(response.getMessage());
    } else {
      console.println("Failed to execute command: " + response.getMessage());
    }
  }

  public Request createRequest(CommandData command, String[] args) {
    if (command.getName().equals("login") || command.getName().equals("register")) {
      sessionManager.setSession(sessionManager.createSession());
    }

    if (command.requiresObject()) {
      Labwork labWork = dataBuilder.buildLabWork();
      labWork.setOwner(sessionManager.getSession().getUsername());
      return new Request(sessionManager.getSession(), command, args, labWork);
    }

    return new Request(sessionManager.getSession(), command, args);
  }

  public Response sendAndReceive(Request request) throws IOException {
    Response response = null;
    boolean received = false;
    int attempts = 0;
    int maxAttempts = 5;

    while (!received && attempts < maxAttempts) {
      try {
        networkManager.send(request);
        response = networkManager.receive();
        received = true;
      } catch (SocketTimeoutException e) {
        attempts++;
        console.println(
            "No response from server (attempt " + attempts + "/" + maxAttempts + "). Retrying...");
      }
    }

    if (response == null) {
      throw new SocketTimeoutException("Could not reconnect to the server");
    }

    return response;
  }
}
