package MsnStructures;

public class TripleEntry<K, V, T> {

  private K key;
  private V value;
  private T third;

  public TripleEntry() {
    key = null;
    value = null;
    third = null;
  }

  public TripleEntry(K key, V value, T third) {
    this.key = key;
    this.value = value;
    this.third = third;
  }

  public K getKey() {
    return key;
  }

  public void setKey(K key) {
    this.key = key;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }

  public T getThird() {
    return third;
  }

  public void setThird(T third) {
    this.third = third;
  }
}
