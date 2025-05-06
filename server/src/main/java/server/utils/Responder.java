package server.utils;

import java.net.SocketAddress;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import server.network.NetworkManager;
import shared.network.Response;

public class Responder implements Runnable {
  private final NetworkManager networkManager;
  private final BlockingQueue<SimpleEntry<SocketAddress, Response>> responseQueue;
  private final Logger logger = Logback.getLogger("Responder");

  public Responder(
      BlockingQueue<SimpleEntry<SocketAddress, Response>> responseQueue,
      NetworkManager networkManager) {
    this.responseQueue = responseQueue;
    this.networkManager = networkManager;
  }

  @Override
  public void run() {
    while (true) {
      SimpleEntry<SocketAddress, Response> entry;
      try {
        entry = responseQueue.take();
        logger.info(
            "Received new response from queue for " + entry.getKey() + ": " + entry.getValue());
      } catch (InterruptedException e) {
        logger.error("Failed to take response from queue: " + e.getMessage());
        return;
      }

      // Send response to user in a separate thread
      new Thread(
              () -> {
                try {
                  networkManager.sendResponse(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                  logger.error("Failed to send response to user: " + e.getMessage());
                }
              })
          .start();
    }
  }
}
