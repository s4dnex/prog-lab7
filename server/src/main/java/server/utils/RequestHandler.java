package server.utils;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.LinkedHashMap;

import server.network.NetworkManager;
import shared.network.Request;
import shared.network.Response;

public class RequestHandler {
    private final Invoker invoker;
    private final NetworkManager networkManager;

    public RequestHandler(Invoker invoker, NetworkManager networkManager) {
        this.invoker = invoker;
        this.networkManager = networkManager;
    }

    public void enable() {
        while (true) {
            LinkedHashMap<SocketAddress, Request> requests;
            
            try {
                requests = networkManager.receiveRequests();
            }
            catch (IOException e) {
                Logback.logger.error("Failed to receive requests from users: " + e.getMessage());
                continue;
            }
            catch (ClassNotFoundException e) {
                Logback.logger.error("Failed to deserialize requests: " + e.getMessage());
                continue;
            }

            for (SocketAddress user : requests.keySet()) {
                Response response;
                
                try {
                    response = invoker.execute(requests.get(user));
                } 
                catch (Exception e) {
                    response = new Response(false, e.getMessage());
                }

                try {
                    networkManager.sendResponse(user, response);
                } 
                catch (IOException e) {
                    Logback.logger.error("Failed to send response to " + user + ": " + e.getMessage());
                }
            }
        }
    }
}
