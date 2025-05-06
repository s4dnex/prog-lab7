package server.utils;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import server.network.NetworkManager;
import shared.network.Request;
import shared.network.Response;

public class RequestHandler {
  private final Invoker invoker;
  private final NetworkManager networkManager;
  private final ExecutorService receivePool;
  private final ExecutorService executePool;
  private final BlockingQueue<SimpleEntry<SocketAddress, Request>> requestQueue;
  private final BlockingQueue<SimpleEntry<SocketAddress, Response>> responseQueue;
  private final Logger logger = Logback.getLogger("RequestHandler");

  public RequestHandler(
      Invoker invoker,
      NetworkManager networkManager,
      BlockingQueue<SimpleEntry<SocketAddress, Request>> requestQueue,
      BlockingQueue<SimpleEntry<SocketAddress, Response>> responseQueue,
      int poolSize) {
    this.invoker = invoker;
    this.networkManager = networkManager;
    this.receivePool = Executors.newFixedThreadPool(poolSize);
    this.executePool = Executors.newFixedThreadPool(poolSize);
    this.requestQueue = requestQueue;
    this.responseQueue = responseQueue;
  }

  public void enable() {
    receivePool.submit(
        () -> {
          while (!Thread.currentThread().isInterrupted()) {
            try {
              LinkedHashMap<SocketAddress, Request> requests = networkManager.receiveRequests();
              requests.forEach(
                  (addr, request) -> {
                    try {
                      requestQueue.put(new SimpleEntry<>(addr, request));
                      logger.info("Put in request queue " + request + " from " + addr);
                    } catch (InterruptedException e) {
                      logger.error("Failed to put request in queue: " + e.getMessage());
                    }
                  });
            } catch (IOException e) {
              logger.error("Failed to receive requests from users: " + e.getMessage());
            } catch (ClassNotFoundException e) {
              logger.error("Failed to deserialize requests: " + e.getMessage());
            }
          }
        });

    executePool.submit(
        () -> {
          while (!Thread.currentThread().isInterrupted()) {
            SimpleEntry<SocketAddress, Request> entry;
            try {
              entry = requestQueue.take();
              logger.info(
                  "Took request from queue " + entry.getValue() + " from " + entry.getKey());
            } catch (InterruptedException e) {
              logger.error("Failed to take request from queue: " + e.getMessage());
              return;
            }

            Response response;

            try {
              response = invoker.execute(entry.getValue());
              logger.info("Executed " + entry.getValue() + " with response " + response);
            } catch (Exception e) {
              logger.error("Failed to execute request: " + e.getMessage());
              response = new Response(false, e.getMessage());
            }

            try {
              responseQueue.put(new SimpleEntry<>(entry.getKey(), response));
              logger.info("Put in response queue " + response + " to " + entry.getKey());
            } catch (InterruptedException e) {
              logger.error("Failed to put response in queue: " + e.getMessage());
            }
          }
        });
  }
}
