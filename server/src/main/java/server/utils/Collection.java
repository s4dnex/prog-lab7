package server.utils;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.LongStream;
import shared.data.*;

/** Class to work with collection. */
public class Collection {
  private final ConcurrentSkipListSet<Labwork> labWorks;

  public Collection() {
    labWorks = new ConcurrentSkipListSet<Labwork>();
  }

  public Collection(Iterable<Labwork> labWorks) {
    this();

    labWorks.forEach(
        lw -> {
          add(lw);
        });
  }

  /**
   * Returns type (class) of collection as String.
   *
   * @return class of collection
   */
  public String getType() {
    return labWorks.getClass().getSimpleName();
  }

  /**
   * Returns current size of collection.
   *
   * @return collection size
   */
  public int getSize() {
    return labWorks.size();
  }

  /**
   * Returns collection as {@link List}
   *
   * @return collection
   */
  public List<Labwork> asList() {
    return labWorks.stream().toList();
  }

  /**
   * Checks if element with given id is in collection.
   *
   * @param id id to check
   * @return {@code true} if element with such id is in collection, otherwise {@code false}
   */
  public boolean contains(long id) {
    return labWorks.stream().anyMatch(lw -> lw.getId().equals(id));
  }

  /**
   * Adds element to collection.
   *
   * @param labWork element to add
   */
  public void add(Labwork labWork) {
    labWorks.add(labWork);
  }

  /**
   * Updates element with given id.
   *
   * @param id id of element to update
   * @param labWork new element
   */
  public void update(Long id, Labwork labWork) {
    labWork.setId(id);

    if (!labWorks.removeIf(lw -> lw.getId().equals(id)))
      throw new IllegalArgumentException("No LabWork with such ID");
    labWorks.add(labWork);
  }

  /**
   * Removes element with given id.
   *
   * @param id id of element to remove
   */
  public void remove(Long id) {
    if (!labWorks.removeIf(lw -> lw.getId().equals(id)))
      throw new IllegalArgumentException("No LabWork with such ID");
  }

  /** Clears the collection from labworks owned by given user. */
  public void clear(String username) {
    labWorks.removeIf(lw -> lw.getOwner().equals(username));
  }

  /**
   * Adds element to collection if it is greater than any other element.
   *
   * @param labWork element to add
   * @return {@code true} if element has been added, otherwise {@code false}
   */
  public boolean addIfMax(Labwork labWork) {
    if (labWorks.size() == 0) {
      add(labWork);
      return true;
    }

    Labwork maxLabWork = labWorks.last();

    if (labWork.compareTo(maxLabWork) > 0) {
      add(labWork);
      return true;
    }

    return false;
  }

  /**
   * Removes every element that is lower than given.
   *
   * @param labWork element for comparison
   * @return number of elements that have been removed
   */
  public long removeLower(Labwork labWork) {
    long count = labWorks.stream().filter(lw -> lw.compareTo(labWork) < 0).count();

    labWorks.removeIf(lw -> lw.compareTo(labWork) < 0);

    return count;
  }

  /**
   * Return sum of minimal points from all elements.
   *
   * @return sum of minimal points
   */
  public long sumOfMinimalPoint() {
    long sum =
        LongStream.of(
                labWorks.stream()
                    .filter(lw -> lw.getMinimalPoint() != null)
                    .mapToLong(lw -> lw.getMinimalPoint())
                    .toArray())
            .sum();
    return sum;
  }

  /**
   * Returns list of all elements difficulties sorted in ascending order.
   *
   * @return list of difficulties
   */
  public List<Difficulty> getAscendingDifficulty() {
    return labWorks.stream().map(lw -> lw.getDifficulty()).sorted().toList();
  }

  /**
   * Returns list of all elements persons sorted in descending order.
   *
   * @return list of persons
   */
  public List<Person> getDescendingAuthor() {
    return labWorks.stream()
        .filter(lw -> lw.getAuthor() != null)
        .map(lw -> lw.getAuthor())
        .sorted(Comparator.reverseOrder())
        .toList();
  }
}
