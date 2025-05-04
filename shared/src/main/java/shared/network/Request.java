package shared.network;

import java.io.Serializable;
import shared.commands.CommandData;

public class Request implements Serializable {
  private static final long serialVersionUID = 1L;
  private CommandData commandData;
  private String[] args;
  private Serializable commandObject;

  public Request(CommandData command, String[] args, Serializable commandObject) {
    this.commandData = command;
    this.args = args;
    this.commandObject = commandObject;
  }

  public Request(CommandData command, String[] args) {
    this(command, args, null);
  }

  public CommandData getCommandData() {
    return commandData;
  }

  public String[] getArgs() {
    return args;
  }

  public Serializable getCommandObject() {
    return commandObject;
  }

  @Override
  public String toString() {
    return "Request {command="
        + commandData
        + ", args=["
        + String.join(", ", args)
        + "], commandObject="
        + commandObject
        + "}";
  }
}
