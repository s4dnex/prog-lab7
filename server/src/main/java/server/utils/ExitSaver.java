package server.utils;

import java.io.IOException;

import server.json.JsonHandler;
import server.network.NetworkManager;
import shared.io.FileHandler;
import shared.utils.Console;

public class ExitSaver implements Runnable {
    private final Collection collection;
    private final FileHandler fileHandler;
    private final Console console;
    private final NetworkManager networkManager;

    public ExitSaver(Collection collection, FileHandler fileHandler, Console console, NetworkManager networkManager) {
        this.collection = collection;
        this.fileHandler = fileHandler;
        this.console = console;
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        Logback.logger.info("Starting server shutdown...");
        try {
            Logback.logger.info("Saving collection to file...");
            fileHandler.write(
                JsonHandler.serializeCollection(collection.asTreeSet())
            );
        } 
        catch (Exception e) {
            Logback.logger.error("Failed to save collection: " + e.getMessage());
        }

        try {
            Logback.logger.info("Closing network manager...");
            networkManager.close();
        } 
        catch (IOException e) {
            Logback.logger.error("Failed to close network manager: " + e.getMessage());
        }

        try {
            Logback.logger.info("Closing console I/O...");
            console.close();
        } 
        catch (IOException e) {
            Logback.logger.error("Failed to close console I/O: " + e.getMessage());
        }
    }
}
