package client;

import client.network.NetworkManager;
import client.utils.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import shared.io.*;
import shared.utils.Console;
import shared.utils.DataBuilder;

public class Main {
  public static void main(String[] args) {
    try (InputHandler inputHandler = new ScannerInput(new Scanner(System.in));
        OutputHandler outputHandler = new SystemOutput();
        Console console = new Console(inputHandler, outputHandler); ) {
      DataBuilder dataBuilder = new DataBuilder(console);
      NetworkManager networkManager =
          new NetworkManager(
              new InetSocketAddress(dataBuilder.getServerAddress(), dataBuilder.getServerPort()));
      console.println("Connection with server is established!");
      CommandHandler invoker = new CommandHandler(console, dataBuilder, networkManager);
      CommandReader commandReader = new CommandReader(console, invoker);
      commandReader.enable();
    } catch (ConnectException e) {
      System.err.println(
          "Failed to connect to the server, please check the server address and port.");
    } catch (SocketTimeoutException e) {
      System.err.println(
          "Connection to the server timed out. Please check your network connection and try"
              + " again.");
    } catch (Exception e) {
      System.err.println("Unexpected program error (" + e.getClass() + "): " + e.getMessage());
      System.err.println("Please, contact the developer. Details of the error:");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
