package server.commands;

import shared.network.Response;

public class Test extends Command {
    private static final long serialVersionUID = 2L;
    public static final Test instance = new Test();

    private Test() {
        super("test");
    }

    @Override
    public Response execute(Object obj, String[] args) {
        return new Response(true, "Request from client was received and processed successfully.");
    }
}