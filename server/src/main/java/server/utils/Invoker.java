package server.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import server.commands.*;
import shared.network.Request;
import shared.network.Response;

/**
 * Class to register commands and invoke their execution.
 */
public class Invoker {
    private final Queue<Command> executedCommands;
    private final Map<String, Command> commands;

    /**
     * @param console Class to handle input and output
     * @param fileHandler Class to handle files
     * @param collection Class to store and work with data
     */
    public Invoker(Collection collection) {
        executedCommands = new LinkedList<Command>();
        commands = new HashMap<String, Command>();

        commands.put(new shared.commands.Add().getName(), new Add(collection));
        commands.put(new shared.commands.AddIfMax().getName(), new AddIfMax(collection));
        commands.put(new shared.commands.Clear().getName(), new Clear(collection));
        commands.put(new shared.commands.Help().getName(), new Help());
        commands.put(new shared.commands.History().getName(), new History(executedCommands));
        commands.put(new shared.commands.Info().getName(), new Info(collection));
        commands.put(new shared.commands.PrintFieldAscendingDifficulty().getName(), new PrintFieldAscendingDifficulty(collection));
        commands.put(new shared.commands.PrintFieldDescendingAuthor().getName(), new PrintFieldDescendingAuthor(collection));
        commands.put(new shared.commands.RemoveById().getName(), new RemoveById(collection));
        commands.put(new shared.commands.RemoveLower().getName(), new RemoveLower(collection));
        commands.put(new shared.commands.Show().getName(), new Show(collection));
        commands.put(new shared.commands.SumOfMinimalPoint().getName(), new SumOfMinimalPoint(collection));
        commands.put(new shared.commands.Test().getName(), new Test());
        commands.put(new shared.commands.Update().getName(), new Update(collection));
    }

    /**
     * Method that parses command from String and executes it
     * @param command Command to execute
     */
    public Response execute(Request request) {
        String commandName = request.getCommandData().getName();
        if (commands.containsKey(commandName)) {
            try {
                Response response = commands.get(commandName)
                                            .execute(request.getArgs(), request.getCommandObject());

                if (executedCommands.size() > 15)
                    executedCommands.poll();
                
                executedCommands.add(commands.get(commandName));    
                return response;
            }
            catch (IllegalArgumentException e) {
                return new Response(false, e.getMessage());
            }
        }
        else {
            return new Response(false, "Command not found. Type 'help' to see available commands.");
        }
    }
}
