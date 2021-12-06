package MsnStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MsnTriplemap<K, V, T>
    implements Map<K, Map.Entry<V, T>>, Iterable<TripleEntry<K, V, T>> {

  MsnMap<K, Map.Entry<V, T>> map;

  public MsnTriplemap() {
    map = new MsnMap<>();
  }

  public void toHashMap() {
    map.toHashMap();
  }

  public void toTreeMap() {
    map.toTreeMap();
  }

  public void toLinkedHashMap() {
    map.toLinkedHashMap();
  }

  public void sortByKey() {
    toTreeMap();
    toLinkedHashMap();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  public boolean containsThird(T third) {
    for (TripleEntry<K, V, T> en : this)
      if (en.getThird().equals(third))
        return true;
    return false;
  }

  public boolean _containsEntry(V value, T third) {
    return map.values().contains(Map.entry(value, third));
  }

  public boolean _containsEntry(K first, V value, T third) {
    for (TripleEntry<K, V, T> en : this)
      if (en.getKey().equals(first) && en.getValue().equals(value) && en.getThird().equals(third))
        return true;
    return false;
  }

  public K _keyForValue(V value) {
    for (Map.Entry<K, Map.Entry<V, T>> en : entrySet())
      if (en.getValue().getKey().equals(value))
        return en.getKey();
    return null;
  }

  public K _keyForThird(T third) {
    for (Map.Entry<K, Map.Entry<V, T>> en : entrySet())
      if (en.getValue().getValue().equals(third))
        return en.getKey();
    return null;
  }

  public K keyForEntry(V value, T third) {
    for (TripleEntry<K, V, T> en : this)
      if (en.getValue().equals(value) && en.getThird().equals(third))
        return en.getKey();
    return null;
  }

  @Override
  public Entry<V, T> get(Object key) {
    return map.get(key);
  }

  public void put(K key, V value, T third) {
    map.put(key, Map.entry(value, third));
  }

  @Override
  public Entry<V, T> put(K key, Entry<V, T> value) {
    return map.put(key, value);
  }

  @Override
  public Entry<V, T> remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends Entry<V, T>> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  public Collection<V> _values() {
    ArrayList<V> v = new ArrayList<>();
    for (Map.Entry<K, Map.Entry<V, T>> en : entrySet())
      v.add(en.getValue().getKey());
    return v;
  }

  public Collection<T> _thirds() {
    ArrayList<T> v = new ArrayList<>();
    for (Map.Entry<K, Map.Entry<V, T>> en : entrySet())
      v.add(en.getValue().getValue());
    return v;
  }

  @Override
  public Collection<Entry<V, T>> values() {
    return map.values();
  }

  public Set<TripleEntry<K, V, T>> _entrySet() {
    LinkedHashSet<TripleEntry<K, V, T>> set = new LinkedHashSet<>();
    for (Map.Entry<K, Map.Entry<V, T>> entry : entrySet())
      set.add(new TripleEntry<K, V, T>(entry.getKey(), entry.getValue().getKey(),
          entry.getValue().getValue()));
    return set;
  }

  @Override
  public Set<Entry<K, Entry<V, T>>> entrySet() {
    return map.entrySet();
  }

  public MsnMap<K, Map.Entry<V, T>> getMap() {
    return map;
  }

  public Map<K, V> keyToValue() {
    LinkedHashMap<K, V> l = new LinkedHashMap<>();
    for (TripleEntry<K, V, T> en : _entrySet())
      l.put(en.getKey(), en.getValue());
    return l;
  }

  public Map<K, T> keyToThird() {
    LinkedHashMap<K, T> l = new LinkedHashMap<>();
    for (TripleEntry<K, V, T> en : _entrySet())
      l.put(en.getKey(), en.getThird());
    return l;
  }

  public String toString() {
    String s = "[";
    int index = 0;
    for (Map.Entry<K, Map.Entry<V, T>> entry : entrySet()) {
      if (index < map.size() - 1)
        s += entry.getKey() + "=" + entry.getValue().getKey() + "=" + entry.getValue().getValue()
            + ", ";
      else
        s += entry.getKey() + "=" + entry.getValue().getKey() + "=" + entry.getValue().getValue();
      index++;
    }
    s += "]";
    return s;
  }

  @Override
  public Iterator<TripleEntry<K, V, T>> iterator() {
    return _entrySet().iterator();
  }
}
