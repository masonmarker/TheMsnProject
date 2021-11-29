package MsnStructures;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import MsnLib.Msn;

/**
 * Class for basic Stream functions.
 * 
 * @author Mason Marker
 * @version 1.0 - 11/24/2021
 * @param <T> Generic
 */
public class MsnStream<T> extends ArrayList<T> {

  private static final long serialVersionUID = 8427462039662763516L;

  public MsnStream() {}

  public MsnStream(T[] arr) {
    addAll(Arrays.asList(arr));
  }

  public MsnStream(Collection<T> collection) {
    addAll(collection);
  }

  public MsnStream(Stream<T> stream) {
    Iterator<T> t = stream.iterator();
    while (t.hasNext())
      add(t.next());
  }


  /**
   * Creates a new MsnStream populated with numbers start-end.
   * 
   * Start is inclusive, end is exclusive
   * 
   * @param start the start
   * @param end the end
   */
  @SuppressWarnings("unchecked")
  public MsnStream(int start, int end) {
    for (int i = start; i < end; i++)
      add((T) Integer.valueOf(i));
  }

  // ---------------- ARRAYLIST EXTENSION ----------------

  public MsnStream<T> _import(Collection<T> collection) {
    addAll(collection);
    return this;
  }

  public <V> MsnStream<T> _importKeys(Map<T, V> map) {
    for (T t : map.keySet())
      add(t);
    return this;
  }

  public <K> MsnStream<T> _importValues(Map<K, T> map) {
    for (Map.Entry<K, T> entry : map.entrySet())
      add(entry.getValue());
    return this;
  }

  /**
   * Imports a range of integers into this MsnStream, should be used only if the Generic is Integer.
   * 
   * @param start the start
   * @param end the end
   * @return
   */
  @SuppressWarnings("unchecked")
  public MsnStream<T> _importRange(int start, int end) {
    for (int i = start; i < end; i++)
      add((T) Integer.valueOf(i));
    return this;
  }

  public MsnStream<T> _add(T t) {
    add(t);
    return this;
  }

  public MsnStream<T> _addAll(T[] t) {
    addAll(List.of(t));
    return this;
  }

  public MsnStream<T> _addDuplicate() {
    addAll(this);
    return this;
  }

  public MsnStream<T> _insert(int index, T t) {
    add(index, t);
    return this;
  }

  public MsnStream<T> _remove(T t) {
    remove(t);
    return this;
  }

  public MsnStream<T> _removeAllOf(T t) {
    while (contains(t))
      remove(t);
    return this;
  }

  public MsnStream<T> _removeAllOf(Collection<T> collection) {
    removeAll(collection);
    return this;
  }

  @SuppressWarnings("unchecked")
  public MsnStream<T> _reorder(T[] reference) {
    return new MsnStream<T>((T[]) Msn.reorder(toArray(), reference));
  }

  /**
   * Removes all elements in this MsnStream.
   * 
   * @return the fixed MsnStream
   */
  public MsnStream<T> _empty() {
    while (size() != 0)
      remove(0);
    return this;
  }

  public MsnStream<T> _set(int index, T t) {
    set(index, t);
    return this;
  }

  public MsnStream<T> _replace(T old, T newelement) {
    return _set(indexOf(old), newelement);
  }

  public MsnStream<T> _replaceAll(T old, T newelement) {
    while (contains(old))
      set(indexOf(old), newelement);
    return this;
  }

  // ---------------- ENTIRE COLLECTION ----------------

  public MsnStream<T> _sorted() {
    T[] a = toGeneric();
    Arrays.sort(a);
    return new MsnStream<T>(a);
  }

  @SuppressWarnings("unchecked")
  public MsnStream<T> _reversed() {
    T[] a = toGeneric();
    Object[] arr = new Object[a.length];
    int index = 0;
    for (int i = a.length - 1; i >= 0; i--) {
      arr[index] = a[i];
      index++;
    }
    return new MsnStream<T>((T[]) arr);
  }

  public MsnStream<T> _shuffled() {
    Collections.shuffle(this);
    return this;
  }

  public MsnStream<T> _withoutDuplicates() {
    LinkedHashSet<T> set = new LinkedHashSet<>(this);
    return new MsnStream<T>(set);
  }

  /**
   * Removes all elements that do not have a duplicate.
   * 
   * @return the fixed MsnStream
   */
  @SuppressWarnings("unchecked")
  public MsnStream<T> _isolateDuplicates() {
    return new MsnStream<T>((T[]) Msn.getDups(toArray()));
  }

  @SuppressWarnings("unchecked")
  public MsnStream<T> _setSize(int newsize) {
    Object[] arr = new Object[newsize];
    if (newsize > size()) {
      Arrays.fill(arr, null);
      for (int i = 0; i < size(); i++)
        arr[i] = get(i);
    } else
      for (int i = 0; i < arr.length; i++)
        arr[i] = get(i);
    return new MsnStream<T>((T[]) arr);
  }

  public MsnStream<T> _doubleSize() {
    return _setSize(size() * 2);
  }

  public MsnStream<T> halfSize() {
    return _setSize(size() / 2);
  }

  public MsnStream<T> _subStream(int fromIndex, int toIndex) {
    return new MsnStream<T>(subList(fromIndex, toIndex));
  }

  public MsnStream<T> _print() {
    System.out.println(this);
    return this;
  }

  public MsnStream<T> _print(String s) {
    System.out.println(s);
    return this;
  }

  // ---------------- FUNCTIONS ----------------

  public MsnStream<T> _filter(Predicate<? super T> predicate) {
    return new MsnStream<T>(stream().filter(predicate));
  }

  public MsnStream<T> _map(Function<? super T, ? extends T> mapper) {
    return new MsnStream<T>(stream().map(mapper));
  }

  public MsnStream<T> _forEach(Consumer<? super T> action) {
    forEach(action);
    return this;
  }

  /**
   * Adds the maximum element to the end of this MsnStream.
   * 
   * @return the fixed Stream
   */
  @SuppressWarnings("unchecked")
  public MsnStream<T> _addMax() {
    try {
      add((T) (Double) Msn.max(Msn.toDouble(toArray())));
    } catch (ClassCastException e) {
      add((T) (Integer) Msn.max(Msn.toInt(toArray())));
    }
    return this;
  }

  /**
   * Adds the minimum element to the end of this MsnStream.
   * 
   * @return the fixed Stream
   */
  @SuppressWarnings("unchecked")
  public MsnStream<T> _addMin() {
    try {
      add((T) (Double) Msn.min(Msn.toDouble(toArray())));
    } catch (ClassCastException e) {
      add((T) (Integer) Msn.min(Msn.toInt(toArray())));
    }
    return this;
  }

  /**
   * Adds the minimum element to the end of this MsnStream.
   * 
   * @return the fixed Stream
   */
  @SuppressWarnings("unchecked")
  public MsnStream<T> _addAvg() {
    try {
      add((T) (Double) Msn.avg(Msn.toDouble(toArray())));
    } catch (ClassCastException e) {
      add((T) (Double) Msn.avg(Msn.toInt(toArray())));
    }
    return this;
  }

  /**
   * Writes each element of this MsnStream to the file passed at one element per line.
   * 
   * @param path the file path
   * @return this MsnStream
   * @throws FileNotFoundException
   */
  public MsnStream<T> _writeTo(String path) throws FileNotFoundException {
    String s = "";
    for (int i = 0; i < size(); i++)
      if (i != size() - 1)
        s += get(i) + "\n";
      else
        s += get(i);
    Msn.writeTo(path, s);
    return this;
  }

  // ---------------- CONVERSION ----------------

  public MsnStream<T> copyOf() {
    MsnStream<T> ret = new MsnStream<>();
    ret.addAll(this);
    return ret;
  }

  public Set<T> toSet() {
    return new LinkedHashSet<>(this);
  }

  @SuppressWarnings("unchecked")
  public T[] toGeneric() {
    return (T[]) toArray();
  }
}
