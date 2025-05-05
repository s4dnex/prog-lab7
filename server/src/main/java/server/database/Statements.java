package server.database;

public enum Statements {
  GET_SALT("SELECT salt FROM users WHERE username = ?"),
  GET_LABWORKS_WITH_FIELDS(
      "SELECT * FROM labworks "
          + "INNER JOIN coordinates USING (coords_id) "
          + "LEFT JOIN persons USING (person_id) "
          + "LEFT JOIN locations USING (loc_id)"),
  GET_COORDINATES_BY_ID("SELECT * FROM coordinates WHERE coords_id = ?"),
  GET_LOCATION_BY_ID("SELECT * FROM locations WHERE loc_id = ?"),
  GET_PERSON_BY_ID("SELECT * FROM persons WHERE person_id = ?"),
  GET_LABWORK_BY_ID("SELECT * FROM labworks WHERE lab_id = ?"),
  GET_LABWORK_WITH_FIELDS(
      "SELECT * FROM labworks "
          + "INNER JOIN coordinates USING (coords_id) "
          + "LEFT JOIN persons USING (person_id) "
          + "LEFT JOIN locations USING (loc_id) "
          + "WHERE lab_id = ?"),
  GET_MAX_LABWORK("SELECT MAX(minimal_point) FROM labworks"),
  GET_NEXT_ID("SELECT nextval('labworks_id_seq')"),
  ADD_USER("INSERT INTO users (username, hash_password, salt) VALUES (?, ?, ?)"),
  ADD_COORDINATES(
      "INSERT INTO coordinates (coords_id, coords_x, coords_y) VALUES (?, ?, ?) RETURNING"
          + " coords_id"),
  ADD_LOCATION(
      "INSERT INTO locations (loc_id, loc_x, loc_y, loc_z, loc_name) VALUES (?, ?, ?, ?, ?)"
          + " RETURNING loc_id"),
  ADD_PERSON(
      "INSERT INTO persons (person_id, person_name, person_weight, person_eye_color,"
          + " person_hair_color, loc_id) VALUES (?, ?, ?, CAST(? as eye_color_enum), CAST(? as"
          + " hair_color_enum), ?) RETURNING person_id"),
  ADD_LABWORK(
      "INSERT INTO labworks (lab_id, lab_name, coords_id, minimal_point, difficulty, person_id,"
          + " owner) VALUES (?, ?, ?, ?, CAST(? as difficulty_enum), ?, ?)"),
  UPDATE_COORDINATES("UPDATE coordinates SET coords_x = ?, coords_y = ? WHERE coords_id = ?"),
  UPDATE_LOCATION(
      "UPDATE locations SET loc_x = ?, loc_y = ?, loc_z = ?, loc_name = ? WHERE loc_id = ?"),
  UPDATE_PERSON(
      "UPDATE persons SET person_name = ?, person_weight = ?, person_eye_color = CAST(? as"
          + " eye_color_enum), person_hair_color = CAST(? as hair_color_enum), loc_id = ? WHERE"
          + " person_id = ?"),
  UPDATE_LABWORK(
      "UPDATE labworks SET lab_name = ?, minimal_point = ?, difficulty = CAST(? as difficulty_enum)"
          + " WHERE lab_id = ?"),
  DELETE_LABWORK_BY_ID("DELETE FROM labworks WHERE lab_id = ?"),
  DELETE_LABWORKS_BY_OWNER("DELETE FROM labworks WHERE owner = ?"),
  DELETE_PERSON_BY_ID("DELETE FROM persons WHERE person_id = ?"),
  DELETE_LOCATION_BY_ID("DELETE FROM locations WHERE loc_id = ?"),
  DELETE_COORDINATES_BY_ID("DELETE FROM coordinates WHERE coords_id = ?"),
  AUTHENTICATE_USER("SELECT * FROM users WHERE username = ? AND hash_password = ?"),
  CHECK_IF_USER_EXISTS("SELECT 1 FROM users WHERE username = ?"),
  CHECK_IF_LABWORK_EXISTS("SELECT 1 FROM labworks WHERE lab_id = ?"),
  CHECK_IF_PERSON_EXISTS("SELECT 1 FROM persons WHERE person_id = ?"),
  CHECK_IF_LOCATION_EXISTS("SELECT 1 FROM locations WHERE loc_id = ?"),
  ;

  private final String statement;

  Statements(String statement) {
    this.statement = statement;
  }

  @Override
  public String toString() {
    return statement;
  }
}
