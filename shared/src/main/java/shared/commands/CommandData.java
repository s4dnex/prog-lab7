package shared.commands;

import java.io.Serializable;

/** Abstract class for commands */
public abstract class CommandData implements Comparable<CommandData>, Serializable {
  private static final long serialVersionUID = 1L;
  protected final String name;
  protected final String[] args;
  protected final boolean requiresObject;
  protected final String description;
  protected final boolean requiresAuth;

  public CommandData(
      String name,
      String[] args,
      boolean requiresObject,
      String description,
      boolean requiresAuth) {
    this.name = name;
    this.args = args;
    this.requiresObject = requiresObject;
    this.description = description;
    this.requiresAuth = requiresAuth;
  }

  public CommandData(String name, String[] args, boolean requiresObject, String description) {
    this(name, args, requiresObject, description, true);
  }

  /**
   * Returns name of {@link CommandData}.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns array of required for {@link CommandData} arguments.
   *
   * @return array of arguments
   */
  public String[] getArgs() {
    return args;
  }

  /**
   * Returns true if {@link CommandData} requires an object to be passed.
   *
   * @return true if requires object, false otherwise
   */
  public boolean requiresObject() {
    return requiresObject;
  }

  /**
   * Returns description of {@link CommandData}
   *
   * @return description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns true if {@link CommandData} requires authentication to be executed.
   *
   * @return true if requires authentication, false otherwise
   */
  public boolean requiresAuth() {
    return requiresAuth;
  }

  @Override
  public int compareTo(CommandData command) {
    return name.compareTo(command.getName());
  }

  @Override
  public boolean equals(Object obj) {
    if (this.getClass() != obj.getClass()) return false;

    CommandData commandData = (CommandData) obj;
    return name.equals(commandData.name)
        && args.length == commandData.args.length
        && requiresObject == commandData.requiresObject
        && description.equals(commandData.description);
  }

  @Override
  public String toString() {
    return "CommandData {name="
        + name
        + ", args=["
        + String.join(", ", args)
        + "], requiresObject="
        + requiresObject
        + ", description="
        + description
        + ", requiresAuth="
        + requiresAuth
        + "}";
  }
}
