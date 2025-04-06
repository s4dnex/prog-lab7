package client.utils;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import client.commands.Command;
import client.network.NetworkManager;
import shared.commands.*;
import shared.data.LabWork;
import shared.io.FileHandler;
import shared.network.*;

/**
 * Class to register commands and invoke their execution.
 */
public class Invoker {
    private final Console console;
    private final NetworkManager networkManager;
    private final FileHandler fileHandler;
    private final DataBuilder dataBuilder;
    private final Map<String, Command> commands;

    /**
     * @param console Class to handle input and output
     * @param fileHandler Class to handle files
     * @param collection Class to store and work with data
     */
    public Invoker(Console console, DataBuilder dataBuilder, NetworkManager networkManager, FileHandler fileHandler) {
        this.console = console;
        this.dataBuilder = dataBuilder;
        this.networkManager = networkManager;
        this.fileHandler = fileHandler;
        this.commands = new HashMap<>();

        registerCommand(new client.commands.Help(console, commands));
        registerCommand(new client.commands.Exit(console));
    }

    /**
     * Method to register command to allow further execution
     * @param command Command to register
     */
    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Method that parses command, checks if it is valid and sends it to the server.
     * @param command Command to execute
     */
    public void handle(String command) {
        SimpleEntry<String, String[]> commandParts = CommandReader.parse(command);
        String commandName = commandParts.getKey();
        String[] args = commandParts.getValue();

        if (!CommandDataManager.has(commandName) && !commands.containsKey(commandName)) {
            String msg = "Command not found. Type 'help' to see available commands.";
            
            if (console.isInteractiveMode())
                console.println(msg);
            else throw new RuntimeException(msg);
            return;
        }

        if (commands.containsKey(commandName)) {
            commands.get(commandName).execute(args);
        }

        if (!CommandDataManager.has(commandName)) {
            return;
        }

        CommandData commandData = CommandDataManager.get(commandName);

        if (args.length != commandData.getArgs().length) {
            console.println("Unexpected arguments occurred, please, try again.");
            return;
        }

        try {
            networkManager.send(createRequest(commandData, args));
        }
        catch (IOException e) {
            console.println("Failed to send request: " + e.getMessage());
            return;
        }

        try {
            Response response = networkManager.receive();
            if (response.isSuccess()) {
                console.println(response.getMessage());
            } 
            else {
                console.println("Failed to execute command: " + response.getMessage());
            }
        } catch (IOException e) {
            console.println("Failed to receive response: " + e.getMessage());
        }
    }

    public Request createRequest(CommandData command, String[] args) {
        if (command.requiresObject()) {
            LabWork labWork = dataBuilder.buildLabWork();
            return new Request(command, args, labWork);
        }

        return new Request(command, args);
    }
}
