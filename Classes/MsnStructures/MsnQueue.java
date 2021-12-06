package MsnStructures;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Simple queue class.
 * 
 * @author Mason Marker
 * @version 1.0 - 10/14/2021
 * @param <T> Generic
 */
public class MsnQueue<T> {

  LinkedList<T> q;

  public MsnQueue() {
    q = new LinkedList<>();
  }

  public MsnQueue(Collection<T> collection) {
    q = new LinkedList<>(collection);
  }

  /**
   * Adds an element to the end of this Queue.
   * 
   * @param t the element
   */
  public void enqueue(T t) {
    q.add(t);
  }

  /**
   * Returns the next element in this Queue.
   * 
   * @param wrap whether the element removed should be moved to the end of this Queue
   * @return the next element
   */
  public T dequeue(boolean wrap) {
    if (wrap) {
      T t = q.pop();
      enqueue(t);
      return t;
    }
    return q.pop();
  }

  /**
   * Gets this Queue in LinkedList representation.
   * 
   * @return the LinkedList
   */
  public LinkedList<T> getList() {
    return q;
  }

  /**
   * String representation.
   */
  public String toString() {
    return q.toString();
  }
}
