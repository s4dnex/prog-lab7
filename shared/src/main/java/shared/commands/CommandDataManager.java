package shared.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandDataManager {
    private static final Map<String, CommandData> commands = new HashMap<String, CommandData>();

    static {
        commands.put(Test.instance.getName(), Test.instance);
        commands.put(Help.instance.getName(), Help.instance);
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
