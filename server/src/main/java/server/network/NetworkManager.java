package server.network;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import org.slf4j.Logger;
import server.utils.Logback;
import shared.network.Request;
import shared.network.Response;

public class NetworkManager implements AutoCloseable {
  private final DatagramChannel channel;
  private final Selector selector;
  private final ByteBuffer buffer = ByteBuffer.allocate(8192);
  private final Logger logger = Logback.getLogger("NetworkManager");

  public NetworkManager(int port) throws IOException {
    channel = DatagramChannel.open();
    channel.bind(new InetSocketAddress("0.0.0.0", port));
    channel.configureBlocking(false);
    selector = Selector.open();
    channel.register(selector, SelectionKey.OP_READ);
    Logback.getLogger().info("Server started on port " + port);
  }

  /** Get all current requests from users. */
  public LinkedHashMap<SocketAddress, Request> receiveRequests()
      throws IOException, ClassNotFoundException {
    LinkedHashMap<SocketAddress, Request> requests = new LinkedHashMap<>();
    if (selector.select(1000) == 0) {
      return requests;
    }
    Set<SelectionKey> selectedKeys = selector.selectedKeys();
    Iterator<SelectionKey> iterator = selectedKeys.iterator();

    while (iterator.hasNext()) {
      SelectionKey key = iterator.next();
      logger.info("Got new request, trying to handle it...");
      if (key.isReadable()) {
        buffer.clear();
        SocketAddress clientAddress = channel.receive(buffer);

        if (clientAddress != null && buffer.position() > 0) {
          buffer.flip();
          try (ByteArrayInputStream bytes = new ByteArrayInputStream(buffer.array());
              ObjectInputStream in = new ObjectInputStream(bytes)) {
            Request request = (Request) in.readObject();
            requests.put(clientAddress, request);
            logger.info("Successfully received " + request + " from " + clientAddress);
          }
        }
      }

      iterator.remove();
    }

    return requests;
  }

  /** Send response to the user. */
  public void sendResponse(SocketAddress clientAddress, Response response) throws IOException {
    logger.info("Sending " + clientAddress + " to " + response);
    try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes)) {
      out.writeObject(response);
      byte[] responseBytes = bytes.toByteArray();
      ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
      channel.send(responseBuffer, clientAddress);
      logger.info("Successfully sent " + response + " to " + clientAddress);
    }
  }

  @Override
  public void close() throws IOException {
    logger.info("Closing server's channel and selector...");
    channel.close();
    selector.close();
  }
}
