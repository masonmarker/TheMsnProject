import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Node for tree development.
 * 
 * @author Mason Marker
 * @version 1.0 - 06/1//2021
 */
public class MsnNode<E> implements Iterable<MsnNode<E>> {

  private ArrayList<MsnNode<E>> nodes;
  private E value;
  
  /**
   * Node constructor
   * 
   * @param value the initial value
   */
  public MsnNode(E value) {
    nodes = new ArrayList<>();
    this.value = value;
  }
  
  /**
   * Adds a child to this node.s
   * 
   * @param value the value of the new node
   */
  public boolean addChild(MsnNode<E> node) {
    return nodes.add(node);
  }
  
  /**
   * Removes a child of this node.
   * 
   * @param node the node to remove
   * @return whether the node was removed or not
   */
  public boolean removeChild(MsnNode<E> node) {
    return nodes.remove(node);
  }
  
  /**
   * Removes a child of this node.
   * 
   * @param index the index of the child to remove
   * @return the node that was removed
   */
  public MsnNode<E> removeChild(int index) {
    return nodes.remove(index);
  }
  
  /**
   * Gets a child of this node at the specified index.
   * 
   * @param index the index of the node
   * @return the child
   */
  public MsnNode<E> getChild(int index) {
    return nodes.get(index);
  }
  
  /**
   * Sets the value of this node.
   * 
   * @param value the value of the node
   */
  public void setValue(E value) {
    this.value = value;
  }
  
  /**
   * Gets the value of this node.
   * 
   * @return the value of this node
   */
  public E value() {
    return value;
  }
  
  /**
   * Gets the children of this node.
   * 
   * @return the children
   */
  public ArrayList<MsnNode<E>> children() {
    return nodes;
  }
  
  /**
   * Gets a Stream of the children of this node.
   * 
   * @return a stream
   */
  public Stream<MsnNode<E>> childStream() {
    return nodes.stream();
  }
  
  /**
   * Gets an Iterator of the children of this node.
   */
  public Iterator<MsnNode<E>> iterator() {
    return nodes.iterator();
  }
  
  /**
   * Equals method.
   * 
   * @param node the node to check
   * @return whether this node equals the node passed
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object obj) {
    return ((MsnNode<E>) obj).value().equals(value);
  }
  
  /**
   * String representation of this node.
   */
  public String toString() {
    return "MsnNode: " + String.valueOf(value);
  }
  
}
