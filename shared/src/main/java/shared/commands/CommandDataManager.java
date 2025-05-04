package shared.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandDataManager {
  private static final Map<String, CommandData> commands = new HashMap<String, CommandData>();

  static {
    register(new Add());
    register(new AddIfMax());
    register(new Clear());
    register(new Help());
    register(new History());
    register(new Info());
    register(new PrintFieldAscendingDifficulty());
    register(new PrintFieldDescendingAuthor());
    register(new RemoveById());
    register(new RemoveLower());
    register(new Show());
    register(new SumOfMinimalPoint());
    register(new Test());
    register(new Update());
  }

  public static void register(CommandData command) {
    commands.put(command.getName(), command);
  }

  public static boolean has(String name) {
    return commands.containsKey(name);
  }

  public static CommandData get(String name) {
    return commands.get(name);
  }

  public static Set<String> keys() {
    return commands.keySet();
  }

  public static Collection<CommandData> values() {
    return commands.values();
  }
}
