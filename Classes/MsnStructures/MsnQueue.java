package MsnStructures;

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
  
  /**
   * Adds an element to the end of this Queue.
   * 
   * @param t the element
   */
  public void add(T t) {
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
      
    }
    return null;
  }
  
  /**
   * Gets this Queue in LinkedList representation.
   * 
   * @return the LinkedList 
   */
  public LinkedList<T> getList() {
    return null;
  }
  
  /**
   * String representation.
   */
  public String toString() {
    return q.toString();
  }
  
  
}
