package MsnStructures;

/**
 * Node for binary tree development.
 * 
 * @author Mason Marker
 * @version 1.0 - 06/11/2021
 * @param <E> value
 */
public class MsnBinaryNode<E> {

  private MsnBinaryNode<E> left;
  private MsnBinaryNode<E> right;
  private E value;
  
  /**
   * Node constructor
   * 
   * @param value the initial value
   */
  public MsnBinaryNode(E value) {
    this.value = value;
    left = null;
    right = null;
  }
  
  /**
   * Sets the left node of this node.
   * 
   * @param node the node
   */
  public void setLeft(MsnBinaryNode<E> node) {
    left = node;
  }
  
  /**
   * Sets the right node of this node.
   * 
   * @param node the node
   */
  public void setRight(MsnBinaryNode<E> node) {
    right = node;
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
   * Gets the left child of this node.
   * 
   * @return the left child
   */
  public MsnBinaryNode<E> left() {
    return left;
  }
  
  /**
   * Gets the right child of this node.
   * 
   * @return the right child
   */
  public MsnBinaryNode<E> right() {
    return right;
  }
  
  /**
   * String representation of this node.
   */
  public String toString() {
    return "MsnBinaryNode: " + String.valueOf(value);
  }
  
}
