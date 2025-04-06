package server.commands;

import shared.network.Response;
import shared.utils.Formatter;
import shared.commands.*;

public class Help extends Command {
    private static final long serialVersionUID = 2L;
    public static final Help instance = new Help();

    private Help() {
        super("help");
    }

    @Override
    public Response execute(Object obj, String[] args) {
        int minColumnWidth = Math.max(11, CommandDataManager.keys()
                                                .stream()
                                                .mapToInt(String::length)
                                                .max()
                                                .orElse(11));

        String format = Formatter.getColumnStringFormat(3, minColumnWidth);        
        StringBuilder msg = new StringBuilder();
        msg.append(String.format(format, "Command", "Arguments", "Description"));

        CommandDataManager.values()
                        .stream()
                        .sorted()
                        .forEach(
                            commandData -> {
                                msg.append(String.format(
                                    format, 
                                    commandData.getName(), 
                                    String.join(", ", commandData.getArgs()), 
                                    commandData.getDescription()));
                                }
                        );

        return new Response(true, msg.toString());
    }
}
