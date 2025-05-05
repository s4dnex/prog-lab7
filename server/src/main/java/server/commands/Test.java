package server.commands;

import shared.network.Request;
import shared.network.Response;

public class Test extends Command {
  private static final long serialVersionUID = 2L;

  public Test() {
    super("test");
  }

  @Override
  public Response execute(Request request) {
    return new Response(true, "Request from client was received and processed successfully.");
  }
}
