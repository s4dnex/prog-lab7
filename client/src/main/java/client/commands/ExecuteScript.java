package client.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import shared.io.DefaultFileHandler;
import shared.utils.Console;
import client.utils.CommandHandler;

/**
 * Command to execute script from a file.
 */
public class ExecuteScript extends Command {
    private final Console console;
    private final CommandHandler commandHandler;
    private final Set<Path> executedScripts = new HashSet<Path>();

    public ExecuteScript(Console console, CommandHandler commandHandler) {
        super(
            "execute_script",
            new String[] {"filePath"},
            "Read and execute the script from the specified file"
        );

        this.console = console;
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) 
            throw new IllegalArgumentException("Unexpected arguments occurred");

        Path path = Path.of(args[0]);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File with such name does not exist.");
        }

        try {
            if (executedScripts.contains(path)) {
                throw new IllegalArgumentException("Recursion detected.");
            }
            executedScripts.add(path);
            Queue<String> script = new DefaultFileHandler(path).readLines();
            console.setScriptMode(script);
            while (!console.getScript().isEmpty()) {
                String command = script.poll();
                // console.println(command);
                commandHandler.handle(command);
            }
        } catch (Exception e) {
            console.println("Failed to execute the script: " + e.getMessage());
        } finally {
            executedScripts.remove(path);
            console.setInteractiveMode();
        }
    }
}
