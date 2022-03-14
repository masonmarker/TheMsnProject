package MsnStructures;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Offers extended Set capabilities.
 * 
 * @author Mason Marker
 * @version 1.0 - 12/07/2021
 */
public class MsnSet<T> implements Set<T>, Iterable<T> {

  Set<T> set;

  public MsnSet() {
    set = new LinkedHashSet<>();
  }

  public MsnSet(Collection<T> collection) {
    set = new LinkedHashSet<>();
    addAll(collection);
  }

  public void toHashSet() {
    set = new HashSet<>(set);
  }

  public void toTreeSet() {
    set = new TreeSet<>(set);
  }

  public void toLinkedHashSet() {
    set = new LinkedHashSet<>(set);
  }

  public T get(int index) {
    int c = 0;
    for (T t : this) {
      if (c == index)
        return t;
      c++;
    }
    return null;
  }

  public int indexOf(T t) {
    int index = 0;
    for (T d : this) {
      if (d.equals(t))
        return index;
      index++;
    }
    return -1;
  }

  @Override
  public int size() {
    return set.size();
  }

  @Override
  public boolean isEmpty() {
    return set.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return set.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return set.iterator();
  }

  @Override
  public Object[] toArray() {
    return set.toArray();
  }

  @SuppressWarnings({"hiding"})
  @Override
  public <T> T[] toArray(T[] a) {
    return set.toArray(a);
  }

  @Override
  public boolean add(T e) {
    return set.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return set.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return set.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return set.addAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return set.retainAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return set.removeAll(c);
  }

  @Override
  public void clear() {
    set.clear();
  }

  public MsnStream<T> toMsnStream() {
    return new MsnStream<T>(set);
  }
}
