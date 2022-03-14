package MsnStructures;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import MsnLib.Msn;

/**
 * Class for advanced Stream functions.
 * 
 * Just keep going...just keep going...just keep going going going...
 * 
 * @author Mason Marker
 * @version 1.1 - 02/26/2022
 * @param <T> Generic
 */
public class MsnStream<T> extends ArrayList<T> {

  private static final long serialVersionUID = 8427462039662763516L;

  /**
   * Default constructor.
   */
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

  public static <L> MsnStream<L> of(Collection<L> collection) {
    return new MsnStream<L>(collection);
  }

  @SuppressWarnings("unchecked")
  public static <L> MsnStream<L> of(Object... objects) {
    return new MsnStream<L>((L[]) objects);
  }
  
  // ---------------- ARRAYLIST EXTENSION ----------------

  public MsnStream<T> _import(Collection<T> collection) {
    addAll(collection);
    return this;
  }

  public MsnStream<T> _import(Stream<T> stream) {
    Iterator<T> t = stream.iterator();
    while (t.hasNext())
      add(t.next());
    return this;
  }

  public MsnStream<T> _import(T[] array) {
    addAll(Arrays.asList(array));
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

  public MsnStream<T> _removeIf(Predicate<? super T> predicate) {
    removeIf(predicate);
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
    T[] a = (T[]) Msn.reorder(toArray(), reference);
    _empty();
    _import(a);
    return this;
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
    _empty();
    _import(a);
    return this;
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
    _empty();
    _import((T[]) arr);
    return this;
  }

  public MsnStream<T> _shuffled() {
    Collections.shuffle(this);
    return this;
  }

  public MsnStream<T> _withoutDuplicates() {
    LinkedHashSet<T> set = new LinkedHashSet<>(this);
    _empty();
    addAll(set);
    return this;
  }

  /**
   * Removes all nulls from this MsnStream.
   * 
   * @return this MsnStream without null values
   */
  public MsnStream<T> _withoutNulls() {
    removeAll(null);
    return this;
  }

  /**
   * Removes all elements that do not have a duplicate.
   * 
   * @return the fixed MsnStream
   */
  @SuppressWarnings("unchecked")
  public MsnStream<T> _isolateDuplicates() {
    T[] a = (T[]) Msn.getDups(toArray());
    _empty();
    _import(a);
    return this;
  }

  /**
   * Sorts the elements in this MsnStream as if it were a HashSet.
   * 
   * @return the fixed MsnStream
   * @since 1.0
   */
  public MsnStream<T> _hashed() {
    HashSet<T> h = new HashSet<>(this);
    _empty();
    addAll(h);
    return this;
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
    _empty();
    _import((T[]) arr);
    return this;
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
    List<T> l = stream().filter(predicate).collect(Collectors.toList());
    _empty();
    addAll(l);
    return this;
  }

  public MsnStream<T> _map(Function<? super T, ? extends T> mapper) {
    List<T> l = stream().map(mapper).collect(Collectors.toList());
    _empty();
    addAll(l);
    return this;
  }

  public MsnStream<T> _forEach(Consumer<? super T> action) {
    forEach(action);
    return this;
  }

  /**
   * Writes each element of this MsnStream to the file passed at one element per line.
   * 
   * @param path the file path
   * @return this MsnStream
   */
  public MsnStream<T> _writeTo(String path) {
    String s = "";
    for (int i = 0; i < size(); i++)
      if (i != size() - 1)
        s += get(i) + "\n";
      else
        s += get(i);
    try {
      Msn.writeTo(path, s);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return this;
  }

  // ---------------- FINAL CONVERSION ----------------

  public <A, R> R collect(Collector<? super T, A, R> collector) {
    return stream().collect(collector);
  }

  /**
   * Obtains basic statistic capabilities for this MsnStream.
   * 
   * @return a new MsnStatistics
   * @since 1.0
   */
  public MsnStatistics statistics() {
    try {
    return new MsnStatistics(Msn.toDouble(toArray()));
    } catch (ClassCastException e) {
      return new MsnStatistics(Msn.toInt(toArray()));
    }
  }

  /**
   * Obtains a copy of this MsnStream.
   * 
   * @return a copy of this MsnStream
   * @since 1.0
   */
  public MsnStream<T> copyOf() {
    MsnStream<T> ret = new MsnStream<>();
    ret.addAll(this);
    return ret;
  }

  /**
   * Converts this MsnStream to a String representation of each element concatinated.
   * 
   * @return a String
   */
  public String toSequence() {
    String s = "";
    for (T t : this)
      s += t;
    return s;
  }

  /**
   * Joins this MsnStream to a String.
   * 
   * @return a String
   * @since 1.1
   */
  public String join() {
    String s = "";
    for (T t : this) {
      s += t;
    }
    return s;
  }
  
  /**
   * Joins this MsnStream with a delimiter.
   * 
   * @param delim the delimiter
   * @return a String
   * @since 1.1
   */
  public String join(String delim) {
    String s = "";
    for (int i = 0; i < size(); i++) {
      if (i != size() - 1) {
        s += get(i) + delim;
      } else {
        s += get(i);
      }
    }
    return s;
  }
  
  /**
   * Creates a Map with keys being each element mapped to null.
   * 
   * @param <K> Generic
   * @return a map representation
   */
  public <V> Map<T, V> toMap() {
    LinkedHashMap<T, V> m = new LinkedHashMap<>();
    for (T t : this)
      m.put(t, null);
    return m;
  }

  /**
   * Obtains an array of Nodes being one Node for each element in this MsnStream.
   * 
   * @return an array of Nodes
   */
  @SuppressWarnings("unchecked")
  public MsnNode<T>[] toNodes() {
    ArrayList<MsnNode<T>> l = new ArrayList<>();
    for (T t : this)
      l.add(new MsnNode<T>(t));
    return l.toArray(MsnNode[]::new);
  }

  /**
   * Obtains a Generic array.
   * 
   * @return a Generic array
   */
  @SuppressWarnings("unchecked")
  public T[] toGeneric() {
    return (T[]) toArray();
  }
}
