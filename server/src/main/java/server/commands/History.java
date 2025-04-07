package server.commands;

import java.util.Queue;

import shared.network.Response;

/**
 * Command to show previously executed commands.
 */
public class History extends Command {
    private final Queue<Command> executedCommands;
    
    public History(Queue<Command> executedCommands) {
        super("history");
        this.executedCommands = executedCommands;
    }

    @Override
    public Response execute(String[] args, Object obj) {
        StringBuilder msg = new StringBuilder("Last executed commands (up to 15):\n");
        executedCommands.forEach(command -> msg.append(command.getName()).append("\n"));
        return new Response(true, msg.toString());
    }
}
