package client.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
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
            close();
            throw new IOException("Failed to connect to server");
        }
    }

    public void send(Request request) throws IOException {
        try (
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes)
        ) {
            out.writeObject(request);
            byte[] data = bytes.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
            clientSocket.send(packet);
        } catch (IOException e) {
            throw new IOException("Failed to send request", e);
        }

        // ByteBuffer buffer = ByteBuffer.wrap(data);
        
        // while (buffer.hasRemaining()) {
        //     clientChannel.write(buffer);
        // }
    }

    public Response receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);
        clientSocket.receive(packet);
        try (
            ByteArrayInputStream bytes = new ByteArrayInputStream(packet.getData());
            ObjectInputStream in = new ObjectInputStream(bytes)
        ) {
            return (Response) in.readObject();
        } catch (IOException|ClassNotFoundException e) {
            throw new IOException("Failed to receive response", e);
        }
    }
    
    private boolean checkConnection() throws IOException {
        send(new Request(Test.instance, new String[0]));
        return receive().isSuccess();
    }

    @Override
    public void close() throws IOException {
        clientSocket.close();
    }
}
