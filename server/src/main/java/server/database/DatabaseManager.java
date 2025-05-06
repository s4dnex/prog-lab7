package server.database;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import server.utils.Logback;
import shared.data.Labwork;

public class DatabaseManager {
  private final Connection connection;
  private final Logger logger = Logback.getLogger("Database");

  public DatabaseManager(Connection connection) throws SQLException, NoSuchAlgorithmException {
    this.connection = connection;
    initialize();
  }

  public void initialize() throws SQLException {
    logger.info("Initializing database...");
    Statement st = connection.createStatement();
    st.executeUpdate(
        """
        CREATE SEQUENCE IF NOT EXISTS labworks_id_seq
        START WITH 1
        INCREMENT BY 1
        """);

    st.executeUpdate(
        """
        CREATE TABLE IF NOT EXISTS users (
            username      TEXT PRIMARY KEY,
            hash_password TEXT NOT NULL,
            salt          TEXT NOT NULL
        )
        """);

    try {
      st.executeUpdate(
          """
          CREATE TYPE difficulty_enum AS ENUM (
              'VERY_EASY',
              'EASY',
              'NORMAL',
              'HARD'
          )
          """);
    } catch (SQLException e) {
      if (!e.getSQLState().equals("42710")) {
        throw e;
      }
    }

    try {
      st.executeUpdate(
          """
          CREATE TYPE eye_color_enum AS ENUM (
              'GREEN',
              'RED',
              'BLACK',
              'WHITE',
              'BROWN'
          )
          """);
    } catch (SQLException e) {
      if (!e.getSQLState().equals("42710")) {
        throw e;
      }
    }

    try {
      st.executeUpdate(
          """
          CREATE TYPE hair_color_enum AS ENUM (
              'RED',
              'ORANGE',
              'BROWN'
          )
          """);
    } catch (SQLException e) {
      if (!e.getSQLState().equals("42710")) {
        throw e;
      }
    }

    st.executeUpdate(
        """
        CREATE TABLE IF NOT EXISTS coordinates (
            coords_id BIGINT PRIMARY KEY,
            coords_x  INTEGER NOT NULL,
            coords_y  REAL    NOT NULL
        )
        """);

    st.executeUpdate(
        """
        CREATE TABLE IF NOT EXISTS locations (
            loc_id   BIGINT             PRIMARY KEY,
            loc_x    DOUBLE PRECISION   NOT NULL,
            loc_y    DOUBLE PRECISION   NOT NULL,
            loc_z    DOUBLE PRECISION   NOT NULL,
            loc_name TEXT               NOT NULL
        )
        """);

    st.executeUpdate(
        """
        CREATE TABLE IF NOT EXISTS persons (
            person_id       BIGINT               PRIMARY KEY,
            person_name     TEXT                 NOT NULL,
            person_weight   REAL  CHECK (person_weight > 0),
            person_eye_color  eye_color_enum,
            person_hair_color hair_color_enum    NOT NULL,
            loc_id          BIGINT NOT NULL REFERENCES locations(loc_id) ON DELETE NO ACTION
        )
        """);

    st.executeUpdate(
        """
        CREATE TABLE IF NOT EXISTS labworks (
            lab_id         BIGINT PRIMARY KEY
                             DEFAULT nextval('labworks_id_seq'),
            lab_name       TEXT                 NOT NULL,
            coords_id      BIGINT  NOT NULL
                             REFERENCES coordinates(coords_id)
                             ON DELETE NO ACTION,
            creation_date  TIMESTAMP
                             DEFAULT CURRENT_TIMESTAMP,
            minimal_point  BIGINT CHECK (minimal_point > 0),
            difficulty     difficulty_enum      NOT NULL,
            person_id      BIGINT
                             REFERENCES persons(person_id)
                             ON DELETE SET NULL,
            owner          TEXT                 NOT NULL
        )
        """);

    st.executeUpdate(
        """
        CREATE OR REPLACE FUNCTION delete_related_labwork_data()
        RETURNS TRIGGER AS $$
        BEGIN
            DELETE FROM coordinates WHERE coords_id = OLD.lab_id;

            DELETE FROM persons WHERE person_id = OLD.lab_id;

            DELETE FROM locations WHERE loc_id = OLD.lab_id;

            RETURN NULL;
        END;
        $$ LANGUAGE plpgsql;
        """);

    st.executeUpdate("DROP TRIGGER IF EXISTS after_labwork_delete ON labworks");

    st.executeUpdate(
        """
        CREATE TRIGGER after_labwork_delete
        AFTER DELETE ON labworks
        FOR EACH ROW
        EXECUTE FUNCTION delete_related_labwork_data();
        """);
  }

  public ResultSet getCollection() throws SQLException {
    PreparedStatement ps =
        connection.prepareStatement(
            Statements.GET_LABWORKS_WITH_FIELDS.toString(),
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
    return ps.executeQuery();
  }

  public ResultSet getSalt(String username) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.GET_SALT.toString());
    ps.setString(1, username);
    return ps.executeQuery();
  }

  public ResultSet getNextId() throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.GET_NEXT_ID.toString());
    return ps.executeQuery();
  }

  public ResultSet getLabworkById(Long id) throws SQLException {
    PreparedStatement ps =
        connection.prepareStatement(Statements.GET_LABWORK_WITH_FIELDS.toString());
    ps.setLong(1, id);
    return ps.executeQuery();
  }

  public ResultSet getMaxLabwork() throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.GET_MAX_LABWORK.toString());
    return ps.executeQuery();
  }

  public boolean checkIfLabworkExists(Long id) throws SQLException {
    PreparedStatement ps =
        connection.prepareStatement(Statements.CHECK_IF_LABWORK_EXISTS.toString());
    ps.setLong(1, id);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }

  public boolean addLabwork(Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.ADD_LABWORK.toString());
    Long id = QueryResultHandler.toId(getNextId());
    labWork.setId(id);
    ps.setLong(1, id);
    ps.setString(2, labWork.getName());
    if (addCoordinates(id, labWork)) {
      ps.setLong(3, id);
    }
    ps.setLong(4, labWork.getMinimalPoint());
    ps.setString(5, labWork.getDifficulty().toString());
    if (labWork.getAuthor() == null) {
      ps.setNull(6, java.sql.Types.BIGINT);
    } else if (addPerson(id, labWork)) {
      ps.setLong(6, id);
    }
    ps.setString(7, labWork.getOwner());
    return ps.executeUpdate() > 0;
  }

  private boolean addCoordinates(Long id, Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.ADD_COORDINATES.toString());
    ps.setLong(1, id);
    ps.setInt(2, labWork.getCoordinates().getX());
    ps.setDouble(3, labWork.getCoordinates().getY());
    ps.execute();
    ResultSet rs = ps.getResultSet();
    if (rs.next() && rs.getLong(1) == id) {
      return true;
    } else {
      throw new SQLException("Failed to add coordinates");
    }
  }

  private boolean addLocation(Long id, Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.ADD_LOCATION.toString());
    ps.setLong(1, id);
    ps.setDouble(2, labWork.getAuthor().getLocation().getX());
    ps.setDouble(3, labWork.getAuthor().getLocation().getY());
    ps.setDouble(4, labWork.getAuthor().getLocation().getZ());
    ps.setString(5, labWork.getAuthor().getLocation().getName());
    ps.execute();
    ResultSet rs = ps.getResultSet();
    if (rs.next() && rs.getInt(1) == id) {
      return true;
    } else {
      throw new SQLException("Failed to add location");
    }
  }

  private boolean checkIfPersonExists(Long id) throws SQLException {
    PreparedStatement ps =
        connection.prepareStatement(Statements.CHECK_IF_PERSON_EXISTS.toString());
    ps.setLong(1, id);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }

  private boolean addPerson(Long id, Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.ADD_PERSON.toString());
    ps.setLong(1, id);
    ps.setString(2, labWork.getAuthor().getName());
    ps.setDouble(3, labWork.getAuthor().getWeight());
    if (labWork.getAuthor().getEyeColor() == null) {
      ps.setNull(4, java.sql.Types.VARCHAR);
    } else {
      ps.setString(4, labWork.getAuthor().getEyeColor().toString());
    }
    ps.setString(5, labWork.getAuthor().getHairColor().toString());
    if (addLocation(id, labWork)) {
      ps.setLong(6, id);
    }
    ps.execute();
    ResultSet rs = ps.getResultSet();
    if (rs.next() && rs.getInt(1) == id) {
      return true;
    } else {
      throw new SQLException("Failed to add person");
    }
  }

  public boolean updateLabwork(Long id, Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.UPDATE_LABWORK.toString());
    ps.setString(1, labWork.getName());
    ps.setLong(2, labWork.getMinimalPoint());
    ps.setString(3, labWork.getDifficulty().toString());
    ps.setLong(4, id);
    ps.executeUpdate();
    updateCoordinates(id, labWork);

    if (!checkIfPersonExists(id) && labWork.getAuthor() != null) {
      addLocation(id, labWork);
      addPerson(id, labWork);
    } else if (checkIfPersonExists(id) && labWork.getAuthor() == null) {
      deletePersonById(id);
      deleteLocationById(id);
    } else if (checkIfPersonExists(id) && labWork.getAuthor() != null) {
      updateLocation(id, labWork);
      updatePerson(id, labWork);
    }

    return true;
  }

  public void updateCoordinates(Long id, Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.UPDATE_COORDINATES.toString());
    ps.setInt(1, labWork.getCoordinates().getX());
    ps.setDouble(2, labWork.getCoordinates().getY());
    ps.setLong(3, id);
    ps.executeUpdate();
  }

  public void updateLocation(Long id, Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.UPDATE_LOCATION.toString());
    ps.setDouble(1, labWork.getAuthor().getLocation().getX());
    ps.setDouble(2, labWork.getAuthor().getLocation().getY());
    ps.setDouble(3, labWork.getAuthor().getLocation().getZ());
    ps.setString(4, labWork.getAuthor().getLocation().getName());
    ps.setLong(5, id);
    ps.executeUpdate();
  }

  public void updatePerson(Long id, Labwork labWork) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.UPDATE_PERSON.toString());
    ps.setString(1, labWork.getAuthor().getName());
    ps.setDouble(2, labWork.getAuthor().getWeight());
    if (labWork.getAuthor().getEyeColor() == null) {
      ps.setNull(3, java.sql.Types.VARCHAR);
    } else {
      ps.setString(3, labWork.getAuthor().getEyeColor().toString());
    }
    ps.setString(4, labWork.getAuthor().getHairColor().toString());
    ps.setLong(5, id);
    ps.executeUpdate();
  }

  public boolean deleteLabworkById(Long id) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.DELETE_LABWORK_BY_ID.toString());
    ps.setLong(1, id);
    return ps.executeUpdate() == 1;
  }

  public boolean deleteLocationById(Long id) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.DELETE_LOCATION_BY_ID.toString());
    ps.setLong(1, id);
    return ps.executeUpdate() == 1;
  }

  public boolean deletePersonById(Long id) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.DELETE_PERSON_BY_ID.toString());
    ps.setLong(1, id);
    return ps.executeUpdate() == 1;
  }

  public boolean deleteCoordinatesById(Long id) throws SQLException {
    PreparedStatement ps =
        connection.prepareStatement(Statements.DELETE_COORDINATES_BY_ID.toString());
    ps.setLong(1, id);
    return ps.executeUpdate() == 1;
  }

  public boolean clear(String username) throws SQLException {
    PreparedStatement ps =
        connection.prepareStatement(Statements.DELETE_LABWORKS_BY_OWNER.toString());
    ps.setString(1, username);
    return ps.executeUpdate() >= 0;
  }

  public boolean addUser(String username, String hashPassword, String salt) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.ADD_USER.toString());
    ps.setString(1, username);
    ps.setString(2, hashPassword);
    ps.setString(3, salt);
    return ps.executeUpdate() == 1;
  }

  public boolean checkIfUserExists(String username) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.CHECK_IF_USER_EXISTS.toString());
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }

  public boolean authenticateUser(String username, String hashPassword) throws SQLException {
    PreparedStatement ps = connection.prepareStatement(Statements.AUTHENTICATE_USER.toString());
    ps.setString(1, username);
    ps.setString(2, hashPassword);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }

  public void close() throws SQLException {
    connection.close();
  }
}
