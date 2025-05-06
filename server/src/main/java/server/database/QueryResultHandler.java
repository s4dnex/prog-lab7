package server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shared.data.Coordinates;
import shared.data.Difficulty;
import shared.data.EyeColor;
import shared.data.HairColor;
import shared.data.Labwork;
import shared.data.Location;
import shared.data.Person;

public class QueryResultHandler {
  public static Iterable<Labwork> toLabWorks(ResultSet data) throws SQLException {
    List<Labwork> labs = new ArrayList<>();
    while (data.next()) {
      data.previous();
      labs.add(toLabWork(data));
    }

    return labs;
  }

  public static Labwork toLabWork(ResultSet data) throws SQLException {
    Labwork lab = null;
    Coordinates coords = null;
    Location location = null;
    Person author = null;

    if (data.next()) {
      coords =
          new Coordinates.Builder()
              .setX(data.getInt("coords_x"))
              .setY(data.getFloat("coords_y"))
              .build();
      data.getInt("loc_id");
      if (!data.wasNull()) {
        location =
            new Location.Builder()
                .setX(data.getDouble("loc_x"))
                .setY(data.getDouble("loc_y"))
                .setZ(data.getDouble("loc_z"))
                .setName(data.getString("loc_name"))
                .build();
      }
      data.getInt("person_id");
      if (!data.wasNull()) {
        author =
            new Person.Builder()
                .setName(data.getString("person_name"))
                .setWeight(data.getFloat("person_weight"))
                .setEyeColor(
                    data.getString("person_eye_color") == null
                        ? null
                        : Enum.valueOf(EyeColor.class, data.getString("person_eye_color")))
                .setHairColor(Enum.valueOf(HairColor.class, data.getString("person_hair_color")))
                .setLocation(location)
                .build();
      }

      lab =
          new Labwork.Builder()
              .setId(data.getLong("lab_id"))
              .setName(data.getString("lab_name"))
              .setCoordinates(coords)
              .setMinimalPoint(data.getLong("minimal_point"))
              .setDifficulty(Enum.valueOf(Difficulty.class, data.getString("difficulty")))
              .setAuthor(author)
              .setOwner(data.getString("owner"))
              .build();
    }

    return lab;
  }

  public static String toLabworkOwner(ResultSet data) throws SQLException {
    String owner = null;
    if (data.next()) {
      owner = data.getString("owner");
    }
    return owner;
  }

  public static Long toId(ResultSet data) throws SQLException {
    Long id = null;
    if (data.next()) {
      id = data.getLong(1);
    }
    return id;
  }

  public static String toString(ResultSet data) throws SQLException {
    String result = null;
    if (data.next()) {
      result = data.getString(1);
    }
    return result;
  }

  public static Long toLong(ResultSet data) throws SQLException {
    Long result = null;
    if (data.next()) {
      result = data.getLong(1);
    }
    return result;
  }
}
