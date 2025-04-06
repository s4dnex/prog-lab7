package client.commands;

import java.util.Map;
import client.utils.Console;
import shared.utils.Formatter;

/**
 * Command to show list of available commands.
 */
public class Help extends Command {
    private final Console console;
    private final Map<String, Command> commands;
    
    public Help(Console console, Map<String, Command> commands) {
        super(
            "help", 
            new String[0],
            "Display available client-side commands"
        );

        this.console = console;
        this.commands = commands;
    }

    @Override
    public void execute(String[] args) {
        int minColumnWidth = Math.max(11, commands.keySet()
                                                .stream()
                                                .mapToInt(String::length)
                                                .max()
                                                .orElse(11));

        String format = Formatter.getColumnStringFormat(3, minColumnWidth);
        StringBuilder msg = new StringBuilder();
        msg.append(String.format(format, "Command", "Arguments", "Description"));

        commands.values()
            .stream()
            .sorted()
            .forEach(
                command -> {
                    msg.append(String.format(
                        format, 
                        command.getName(), 
                        String.join(", ", command.getArgs()), 
                        command.getDescription()
                    ));
                }
            );
        
        console.println(msg.toString());
    }
}
