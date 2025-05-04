package server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import server.json.JsonHandler;
import server.network.NetworkManager;
import server.utils.*;
import shared.io.*;
import shared.utils.*;

public class Main {
  public static void main(String[] args) {
    if (args.length != 1) {
      Logback.logger.error("Pass collection file path as argument.");
      System.exit(1);
    }

    Path path = Path.of(args[0]);
    if (Files.notExists(path)) {
      Logback.logger.error("File with such name does not exist.");
      System.exit(1);
    }

    try (InputHandler inputHandler = new ScannerInput(new Scanner(System.in));
        OutputHandler outputHandler = new SystemOutput();
        Console console = new Console(inputHandler, outputHandler); ) {
      FileHandler fileHandler = new DefaultFileHandler(path);
      Collection collection = new Collection(JsonHandler.deserializeCollection(fileHandler.read()));

      DataBuilder dataBuilder = new DataBuilder(console);
      NetworkManager networkManager = new NetworkManager(dataBuilder.getServerPort());
      Invoker invoker = new Invoker(collection);
      RequestHandler requestHandler = new RequestHandler(invoker, networkManager);
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(new ExitSaver(collection, fileHandler, console, networkManager)));

      requestHandler.enable();
    } catch (Exception e) {
      Logback.logger.error("Unexpected program error (" + e.getClass() + "): " + e.getMessage());
      Logback.logger.error(
          "Details of the error: "
              + String.join(
                  "\n",
                  Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()));
      System.exit(1);
    }
  }
}
