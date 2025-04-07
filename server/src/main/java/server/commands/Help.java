package server.commands;

import shared.network.Response;
import shared.utils.Formatter;
import shared.commands.*;

public class Help extends Command {
    private static final long serialVersionUID = 2L;

    public Help() {
        super("help");
    }

    @Override
    public Response execute(String[] args, Object obj) {
        int minColumnWidth = Math.max(11, CommandDataManager.keys()
                                                .stream()
                                                .mapToInt(String::length)
                                                .max()
                                                .orElse(11));

        String format = Formatter.getColumnStringFormat(3, minColumnWidth);        
        StringBuilder msg = new StringBuilder();
        msg.append("Server-side commands:\n");
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
