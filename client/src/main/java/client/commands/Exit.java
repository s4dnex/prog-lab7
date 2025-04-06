package client.commands;

import client.utils.Console;

/**
 * Command to exit the program.
 */
public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super(
            "exit", 
            new String[0], 
            "Exit the program"
        );

        this.console = console;
    }

    @Override
    public void execute(String[] args) {
        try {
            console.print("Exiting the program...");
            console.close();
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Couldn't close the console's I/O handlers.");
            System.exit(1);
        }
    }
}
