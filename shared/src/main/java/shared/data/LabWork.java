package shared.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Labwork implements Comparable<Labwork>, Serializable {
  private static final long serialVersionUID = 1L;
  private Long id; // != null, > 0, unique, auto
  private String name; // != null, != empty
  private Coordinates coordinates; // != null
  private LocalDateTime creationDate; // != null, auto
  private Long minimalPoint; // > 0
  private Difficulty difficulty; // != null
  private Person author;
  private String owner; // != null, != empty

  /**
   * @param builder {@link Builder}
   */
  public Labwork(Labwork.Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.coordinates = builder.coordinates;
    this.minimalPoint = builder.minimalPoint;
    this.difficulty = builder.difficulty;
    this.author = builder.author;
    this.owner = builder.owner;
    this.creationDate = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public Long getMinimalPoint() {
    return minimalPoint;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public Person getAuthor() {
    return author;
  }

  public String getOwner() {
    return owner;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public static boolean validateId(Long id) {
    if (id == null || id <= 0) return false;
    return true;
  }

  public static boolean validateName(String name) {
    if (name == null || name.isBlank()) return false;
    return true;
  }

  public static boolean validateCoordinates(Coordinates coordinates) {
    if (coordinates == null) return false;
    return true;
  }

  public static boolean validateMinimalPoint(Long minimalPoint) {
    if (minimalPoint != null && minimalPoint <= 0) return false;
    return true;
  }

  public static boolean validateDifficulty(Difficulty difficulty) {
    if (difficulty == null) return false;
    return true;
  }

  public static boolean validateAuthor(Person author) {
    return true;
  }

  @Override
  public int compareTo(Labwork labWork) {
    if (this.difficulty == labWork.difficulty) {
      if (this.minimalPoint == null && labWork.minimalPoint == null) return 0;
      else if (this.minimalPoint == null) return -1;
      else if (labWork.minimalPoint == null) return 1;
      else return this.minimalPoint.compareTo(labWork.minimalPoint);
    } else return this.difficulty.compareTo(labWork.difficulty);
  }

  @Override
  public String toString() {
    return "LabWork {"
        + "id="
        + id
        + ", name=\""
        + name
        + "\""
        + ", coordinates="
        + coordinates
        + ", creationDate="
        + creationDate
        + ", minimalPoint="
        + minimalPoint
        + ", difficulty="
        + difficulty
        + ", author="
        + author
        + ", owner=\""
        + owner
        + "\""
        + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (this.getClass() != obj.getClass()) return false;

    Labwork lw = (Labwork) obj;
    return this.name.equals(lw.name)
        && this.coordinates.equals(lw.coordinates)
        && this.minimalPoint.equals(lw.minimalPoint)
        && this.difficulty.equals(lw.difficulty)
        && this.author.equals(lw.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, coordinates, minimalPoint, difficulty, author);
  }

  /** Class to build {@link Labwork}. */
  public static class Builder {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private Long minimalPoint;
    private Difficulty difficulty;
    private Person author;
    private String owner;

    public Builder setId(Long id) {
      this.id = id;
      return this;
    }

    public Builder setOwner(String owner) {
      this.owner = owner;
      return this;
    }

    /**
     * Sets value of field {@code name}.
     *
     * @param name name
     * @return {@link Builder} instance
     */
    public Builder setName(String name) {
      if (validateName(name)) {
        this.name = name;
        return this;
      }
      throw new IllegalArgumentException();
    }

    /**
     * Sets value of field {@code coordinates}.
     *
     * @param coordinates coordinates
     * @return {@link Builder} instance
     */
    public Builder setCoordinates(Coordinates coordinates) {
      if (validateCoordinates(coordinates)) {
        this.coordinates = coordinates;
        return this;
      }
      throw new IllegalArgumentException();
    }

    /**
     * Sets value of field {@code minimalPoint}.
     *
     * @param minimalPoint minimal points
     * @return {@link Builder} instance
     */
    public Builder setMinimalPoint(Long minimalPoint) {
      if (validateMinimalPoint(minimalPoint)) {
        this.minimalPoint = minimalPoint;
        return this;
      }
      throw new IllegalArgumentException();
    }

    /**
     * Sets value of field {@code difficulty}.
     *
     * @param difficulty difficulty
     * @return {@link Builder} instance
     */
    public Builder setDifficulty(Difficulty difficulty) {
      if (validateDifficulty(difficulty)) {
        this.difficulty = difficulty;
        return this;
      }
      throw new IllegalArgumentException();
    }

    /**
     * Sets value of field {@code author}.
     *
     * @param author author
     * @return {@link Builder} instance
     */
    public Builder setAuthor(Person author) {
      if (validateAuthor(author)) {
        this.author = author;
        return this;
      }
      throw new IllegalArgumentException();
    }

    /**
     * Returns new {@link Labwork} instance with set fields.
     *
     * @return {@link Labwork}
     */
    public Labwork build() {
      if (validateName(name)
          && validateCoordinates(coordinates)
          && validateMinimalPoint(minimalPoint)
          && validateDifficulty(difficulty)
          && validateAuthor(author)) return new Labwork(this);
      throw new IllegalArgumentException("Invalid LabWork parameters");
    }
  }
}
