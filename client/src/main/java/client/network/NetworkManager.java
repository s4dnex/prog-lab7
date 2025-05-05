package client.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import shared.commands.Test;
import shared.network.*;

public class NetworkManager implements AutoCloseable {
  private final DatagramSocket clientSocket;
  private final SocketAddress serverAddress;

  public NetworkManager(SocketAddress serverAddress) throws IOException {
    this.serverAddress = serverAddress;

    clientSocket = new DatagramSocket();
    clientSocket.setSoTimeout(5000);

    if (!checkConnection()) {
      throw new ConnectException("Failed to connect to the server");
    }
  }

  public void send(Request request) throws IOException {
    try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes)) {
      out.writeObject(request);
      byte[] data = bytes.toByteArray();
      DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
      clientSocket.send(packet);
    } catch (SocketTimeoutException e) {
      throw new SocketTimeoutException("Lost connection to the server");
    } catch (IOException e) {
      throw new IOException("Failed to send a request", e);
    }
  }

  public Response receive() throws IOException {
    DatagramPacket packet = new DatagramPacket(new byte[8192], 8192);
    clientSocket.receive(packet);
    try (ByteArrayInputStream bytes = new ByteArrayInputStream(packet.getData());
        ObjectInputStream in = new ObjectInputStream(bytes)) {
      return (Response) in.readObject();
    } catch (SocketTimeoutException e) {
      throw new SocketTimeoutException("Lost connection to the server");
    } catch (IOException | ClassNotFoundException e) {
      throw new IOException("Failed to receive a response", e);
    }
  }

  private boolean checkConnection() throws IOException {
    try {
      send(new Request(null, new Test(), new String[0]));
      return receive().isSuccess();
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public void close() throws IOException {
    clientSocket.close();
  }
}
