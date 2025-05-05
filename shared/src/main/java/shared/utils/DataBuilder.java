package shared.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import shared.data.*;

/** Class to build data for collection. */
public class DataBuilder {
  private final Console console;

  /**
   * @param console Class to handle input and output
   */
  public DataBuilder(Console console) {
    this.console = console;
  }

  public String getUsername() {
    while (true) {
      try {
        String login = getString("Enter your username: ");
        if (login == null || login.isBlank()) {
          throw new IllegalArgumentException();
        }
        return login;
      } catch (IllegalArgumentException e) {
        handleException("Username cannot be null or empty!");
      }
    }
  }

  public String getPassword() {
    while (true) {
      try {
        String password = getString("Enter your password: ");
        if (password == null || password.isBlank()) {
          throw new IllegalArgumentException();
        }
        return password;
      } catch (IllegalArgumentException e) {
        handleException("Password cannot be null or empty!");
      }
    }
  }

  public InetAddress getServerAddress() {
    while (true) {
      try {
        String address = getString("Enter the server address (default: localhost): ");

        if (address == null || address.isBlank()) {
          return InetAddress.getLocalHost();
        }

        return InetAddress.getByName(address);
      } catch (UnknownHostException e) {
        handleException("Unknown host, please check the address and try again.");
      }
    }
  }

  public int getServerPort() {
    while (true) {
      try {
        Integer port = getInt("Enter the server port (default: 1234): ");

        if (port == null) {
          return 1234;
        }

        if (port < 1 || port > 65535) {
          throw new IllegalArgumentException();
        }

        return port;
      } catch (IllegalArgumentException e) {
        handleException("Invalid port, please enter a valid number [1 - 65535].");
      }
    }
  }

  /**
   * Method to set fields and build {@link Labwork}.
   *
   * @return LabWork instance
   */
  public Labwork buildLabWork() {
    Labwork.Builder lwBuilder = new Labwork.Builder();

    while (true) {
      try {
        lwBuilder.setName(getString("Enter the labwork's name: "));
        break;
      } catch (IllegalArgumentException e) {
        handleException("Name cannot be null or empty!");
      }
    }

    while (true) {
      try {
        lwBuilder.setMinimalPoint(getLong("Enter the labwork's minimal point: "));
        break;
      } catch (IllegalArgumentException e) {
        handleException("Minimal point must be a long integer greater than 0 or null!");
      }
    }

    while (true) {
      try {
        lwBuilder.setDifficulty(
            getEnum(
                Difficulty.class,
                "Enter the labwork's difficulty ("
                    + Formatter.getEnumValues(Difficulty.class)
                    + "): "));
        break;
      } catch (IllegalArgumentException | NullPointerException e) {
        handleException("Difficulty must be one if available values!");
      }
    }

    return lwBuilder.setCoordinates(buildCoordinates()).setAuthor(buildPerson()).build();
  }

  /**
   * Method to set fields and build {@link Coordinates}.
   *
   * @return Coordinates instance
   */
  public Coordinates buildCoordinates() {
    Coordinates.Builder coordsBuilder = new Coordinates.Builder();

    while (true) {
      try {
        coordsBuilder.setX(getInt("Enter the X coordinate: "));
        break;
      } catch (IllegalArgumentException | NullPointerException e) {
        handleException("X coordinate must be an integer!");
      }
    }

    while (true) {
      try {
        coordsBuilder.setY(getFloat("Enter the Y coordinate: "));
        break;
      } catch (IllegalArgumentException | NullPointerException e) {
        handleException("Y coordinate must be a float number!");
      }
    }

    return coordsBuilder.build();
  }

  /**
   * Method to set fields and build {@link Location}.
   *
   * @return Location instance
   */
  public Location buildLocation() {
    Location.Builder locationBuilder = new Location.Builder();

    while (true) {
      try {
        locationBuilder.setName(getString("Enter the location's name: "));
        break;
      } catch (IllegalArgumentException e) {
        handleException("Name can't be null or empty!");
      }
    }

    while (true) {
      try {
        locationBuilder.setX(getDouble("Enter the location's X coordinate: "));
        break;
      } catch (IllegalArgumentException | NullPointerException e) {
        handleException("X coordinate must be a double number!");
      }
    }

    while (true) {
      try {
        locationBuilder.setY(getDouble("Enter the location's Y coordinate: "));
        break;
      } catch (IllegalArgumentException | NullPointerException e) {
        handleException("Y coordinate must be a double number!");
      }
    }

    while (true) {
      try {
        locationBuilder.setZ(getDouble("Enter the location's Z coordinate: "));
        break;
      } catch (NullPointerException | IllegalArgumentException e) {
        handleException("Z coordinate must be a double number!");
      }
    }

    return locationBuilder.build();
  }

  /**
   * Method to set fields and build {@link Person}.
   *
   * @return Person instance
   */
  public Person buildPerson() {
    Person.Builder personBuilder = new Person.Builder();

    while (true) {
      try {
        personBuilder.setName(
            getString("Enter the person's name (if you want to skip, enter the blank line): "));
        break;
      } catch (IllegalArgumentException e) {
        return null;
        // handleException("Name can't be null or empty!");
      }
    }

    while (true) {
      try {
        personBuilder.setWeight(getFloat("Enter the person's weight: "));
        break;
      } catch (NullPointerException | IllegalArgumentException e) {
        handleException("Weight must be a float number greater than 0!");
      }
    }

    while (true) {
      try {
        personBuilder.setEyeColor(
            getEnum(
                EyeColor.class,
                "Enter the person's eye color ("
                    + Formatter.getEnumValues(EyeColor.class)
                    + "): "));
        break;
      } catch (IllegalArgumentException | NullPointerException e) {
        handleException("Eye color must be one of available values or null!");
      }
    }

    while (true) {
      try {
        personBuilder.setHairColor(
            getEnum(
                HairColor.class,
                "Enter the person's hair color ("
                    + Formatter.getEnumValues(HairColor.class)
                    + "): "));
        break;
      } catch (IllegalArgumentException | NullPointerException e) {
        handleException("Hair color must be one if available values!");
      }
    }

    return personBuilder.setLocation(buildLocation()).build();
  }

  /**
   * Method to receive {@link String} from input.
   *
   * @param prompt Text to request field
   * @return {@link String} (or {@code null})
   */
  public String getString(String prompt) {
    if (console.isInteractiveMode()) console.print(prompt);
    return console.readln();
  }

  /**
   * Method to receive {@link Integer} from input.
   *
   * @param prompt Text to request field
   * @return {@link Integer} (or {@code null})
   */
  public Integer getInt(String prompt) {
    if (console.isInteractiveMode()) console.print(prompt);

    String line = console.readln();
    if (line == null) return null;
    return Integer.parseInt(line);
  }

  /**
   * Method to receive {@link Long} from input.
   *
   * @param prompt Text to request field
   * @return {@link Long} (or {@code null})
   */
  public Long getLong(String prompt) {
    if (console.isInteractiveMode()) console.print(prompt);

    String line = console.readln();
    if (line == null) return null;
    return Long.parseLong(line);
  }

  /**
   * Method to receive {@link Float} from input.
   *
   * @param prompt Text to request field
   * @return {@link Float} (or {@code null})
   */
  public Float getFloat(String prompt) {
    if (console.isInteractiveMode()) console.print(prompt);

    String line = console.readln();
    if (line == null) return null;
    return Float.parseFloat(line);
  }

  /**
   * Method to receive {@link Double} from input.
   *
   * @param prompt Text to request field
   * @return {@link Double} (or {@code null})
   */
  public Double getDouble(String prompt) {
    if (console.isInteractiveMode()) console.print(prompt);

    String line = console.readln();
    if (line == null) return null;
    return Double.parseDouble(line);
  }

  /**
   * Method to receive one of {@link Enum} values from input.
   *
   * @param prompt Text to request field
   * @return One of {@link Enum} values (or {@code null})
   */
  public <T extends Enum<T>> T getEnum(Class<T> enumType, String prompt) {
    if (console.isInteractiveMode()) console.print(prompt);

    String line = console.readln();
    if (line == null || line.isBlank()) return null;
    else return Enum.valueOf(enumType, line.toUpperCase());
  }

  /**
   * Method to handle exception with given message. Prints message to console if {@link Console} in
   * interactive mode, throws {@link RuntimeException} if {@link Console} in script mode.
   *
   * @param msg Message from exception
   */
  private void handleException(String msg) {
    if (console.isInteractiveMode()) console.println(msg);
    else throw new RuntimeException(msg);
  }
}
