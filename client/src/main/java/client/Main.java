package client;

import java.net.InetSocketAddress;
import java.util.Scanner;
import client.network.NetworkManager;
import client.utils.*;
import shared.io.*;

public class Main {
    public static void main(String[] args) {
        try (
            InputHandler inputHandler = new ScannerInput(new Scanner(System.in));
            OutputHandler outputHandler = new SystemOutput();
            Console console = new Console(inputHandler, outputHandler);
        ) {
            DataBuilder dataBuilder = new DataBuilder(console);
            NetworkManager networkManager = new NetworkManager(
                new InetSocketAddress(dataBuilder.getServerAddress(), dataBuilder.getServerPort())
            );
            console.println("Connection with server is established!");
            Invoker invoker = new Invoker(console, dataBuilder, networkManager, null);
            CommandReader commandReader = new CommandReader(console, invoker);
            commandReader.enable();
        }
        catch (Exception e) {
            System.err.println("Unexpected program error (" + e.getClass() + "): " + e.getMessage());
            System.err.println("Please, contact the developer. Details of the error:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
