package shared.network;

import java.io.Serializable;
import shared.commands.CommandData;

public class Request implements Serializable {
  private static final long serialVersionUID = 1L;
  private Session session;
  private CommandData commandData;
  private String[] args;
  private Serializable commandObject;

  public Request(Session session, CommandData command, String[] args, Serializable commandObject) {
    this.session = session;
    this.commandData = command;
    this.args = args;
    this.commandObject = commandObject;
  }

  public Request(Session session, CommandData command, String[] args) {
    this(session, command, args, null);
  }

  public Session getSession() {
    return session;
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
    return "Request {session="
        + session
        + ", command="
        + commandData
        + ", args=["
        + String.join(", ", args)
        + "], commandObject="
        + commandObject
        + "}";
  }
}
