package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import server.commands.*;
import shared.network.Request;
import shared.network.Response;

public class Main {
    private static final HashMap<String, Command> commands = new HashMap<>();
    static {
        commands.put(Test.instance.getName(), Test.instance);
        commands.put(Help.instance.getName(), Help.instance);
        // Add other commands here
    }

    public static void main(String[] args) {
        try {
            DatagramChannel channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(1234));
            channel.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(2048);

            System.out.println("Server is launched!");

            while (true) {
                buffer.clear();
                InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);

                if (clientAddress != null) {
                    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
                    Request request = (Request) in.readObject();
                    // buffer.flip();
                    // byte[] data = new byte[buffer.remaining()];
                    // buffer.get(data);
                    // String message = new String(data, StandardCharsets.UTF_8);
                    System.out.println("Received message: " + request + " from " + clientAddress);

                    if (request.getCommandData() == null || !commands.containsKey(request.getCommandData().getName())) {
                        System.out.println("Unknown command: " + request.getCommandData());
                        continue;
                    }

                    // Echo the message back to the client
                    Response response = commands.get(request.getCommandData().getName()).execute(request.getCommandObject(),request.getCommandData().getArgs());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(bytes);
                    out.writeObject(response);

                    var responseBytes = bytes.toByteArray();
                    
                    ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
                    channel.send(responseBuffer, clientAddress);
                }

                Thread.sleep(1000); // Sleep for a while to avoid busy waiting
            }
        } catch (IOException|InterruptedException|ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
