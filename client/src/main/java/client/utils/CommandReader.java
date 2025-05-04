package client.utils;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import shared.utils.Console;

/** Class to read and parse {@link commands.Command}. */
public class CommandReader {
  private final Console console;
  private final CommandHandler invoker;

  // CONSTRUCTORS

  /**
   * @param console Class to handle input and output
   * @param invoker Class to invoke commands execution
   */
  public CommandReader(Console console, CommandHandler invoker) {
    this.console = console;
    this.invoker = invoker;
  }

  // METHODS

  /**
   * Parses {@link commands.Command} from {@link String} and returns it as array.
   *
   * @param command Command to parse
   * @return Array with command name and arguments
   */
  public static SimpleEntry<String, String[]> parse(String command) {
    if (command == null || command.isBlank()) {
      return new SimpleEntry<String, String[]>("", new String[0]);
    }

    String[] commandParts = command.trim().split("\s+");
    String name = commandParts[0].toLowerCase();
    String[] args = new String[Math.max(0, commandParts.length - 1)];

    for (int i = 1; i < commandParts.length; i++) {
      args[i - 1] = commandParts[i];
    }

    return new SimpleEntry<>(name, args);
  }

  /** Method to enable {@link CommandReader} and pass input to {@link CommandHandler}. */
  public void enable() throws IOException {
    console.setInteractiveMode();

    while (true) {
      console.print("> ");
      invoker.handle(console.readln());
    }
  }
}
