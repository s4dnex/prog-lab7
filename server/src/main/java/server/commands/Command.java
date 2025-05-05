package server.commands;

import java.io.Serializable;

import shared.network.Request;
import shared.network.Response;

public abstract class Command implements Serializable {
  private static final long serialVersionUID = 1L;
  protected final String name;

  public Command(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public abstract Response execute(Request request);

  @Override
  public String toString() {
    return "Command {name=" + name + "}";
  }
}
