package MsnStructures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class for advanced Map capabilities.
 * 
 * @author Mason Marker
 * @version 1.0 - 12/02/2021
 */
public class MsnMap<K, V> implements Map<K, V> , Iterable<Map.Entry<K, V>>{

  Map<K, V> map;

  public MsnMap() {
    map = new LinkedHashMap<>(); 
  }

  public void toHashMap() {
    map = new HashMap<>(this); 
  }

  public void toTreeMap() {
    map = new TreeMap<>(this);
  }

  public void toLinkedHashMap() {
    map = new LinkedHashMap<>(this);
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

  public boolean containsEntry(K key, V value) {
    for (Map.Entry<K, V> en : entrySet())
      if (en.getKey().equals(key) && en.getValue().equals(value))
        return true;
    return false;
  }

  @Override
  public V get(Object key) {
    return map.get(key);
  }

  public Map.Entry<K, V> getAt(int index) {
    return toArray()[index];
  }

  public K keyFor(V value) {
    for (Map.Entry<K, V> en : entrySet())
      if (en.getValue().equals(value))
        return en.getKey();
    return null;
  }

  @Override
  public V put(K key, V value) {
    return map.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
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

  public Collection<K> keys() {
    return keySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return map.entrySet();
  }

  @SuppressWarnings("unchecked")
  public Map.Entry<K, V>[] toArray() {
    return entrySet().toArray(Map.Entry[]::new);
  }

  public MsnStream<Map.Entry<K, V>> toMsnStream() {
    return new MsnStream<Map.Entry<K, V>>(entrySet());
  }

  public MsnMultimap<K, V> toMultimap() {
    MsnMultimap<K, V> m = new MsnMultimap<>();
    for (Map.Entry<K, V> entry : entrySet())
      m.put(entry.getKey(), entry.getValue());
    return m;
  }

  public Map<K, V> getMap() {
    return map;
  }

  public String toString() {
    return map.toString();
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    return map.entrySet().iterator();
  }
}
