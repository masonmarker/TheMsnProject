package MsnStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class for Multimap capabilities.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/03/2021
 */
public class MsnMultimap<K, V> implements Iterable<Map.Entry<K, V>> {

  private Map<K, ArrayList<V>> map;

  /**
   * Multimap constructor.
   */
  public MsnMultimap() {
    map = new LinkedHashMap<>();
  }

  public void toHashMap() {
    map = new HashMap<K, ArrayList<V>>(map);
  }

  public void toTreeMap() {
    map = new TreeMap<K, ArrayList<V>>(map);
  }

  public void toLinkedHashMap() {
    map = new LinkedHashMap<K, ArrayList<V>>(map);
  }

  /**
   * Method for distributing elements.
   * 
   * @param k k
   * @param v v
   */
  public void put(K k, V v) {
    ArrayList<V> curr = map.get(k);
    if (curr == null) {
      ArrayList<V> list = new ArrayList<>();
      list.add(v);
      map.put(k, list);
    } else
      curr.add(v);
  }

  /**
   * Gets the set of values for the specified key.
   * 
   * @param key the key
   * @return the list of values that correspond to the key
   */
  public ArrayList<V> get(K key) {
    return map.get(key);
  }

  /**
   * Checks if this map contains the key specified.
   * 
   * @param key the key to search for
   * @return whether the key was found or not
   */
  public boolean containsKey(K key) {
    return map.containsKey(key);
  }

  /**
   * Checks if this map contains the value specified.
   * 
   * @param value the value to search for
   * @return whether the value was found or not
   */
  public boolean containsValue(V value) {
    Iterator<Entry<K, V>> i = iterator();
    while (i.hasNext())
      if (i.next().getValue().equals(value))
        return true;
    return false;
  }

  public boolean containsEntry(K key, V value) {
    for (Map.Entry<K, V> entry : entrySet())
      if (entry.getKey().equals(key) && entry.getValue().equals(value))
        return true;
    return false;
  }

  /**
   * Removes the entry for the key passed.
   * 
   * @param key the key to remove
   * @return the collection of values mapped to the key removed
   */
  public Collection<V> remove(K key) {
    return map.remove(key);
  }

  /**
   * Removes the first instance of 'value', and returns the key that was mapped to that value.
   * 
   * --The key is removed if it is no longer mapped to any values after removing 'value'--
   * 
   * @param value the value to remove
   * @return the key mapped to the value removed
   */
  public void removeValue(V value) {
    for (Map.Entry<K, ArrayList<V>> en : map.entrySet())
      if (en.getValue().remove(value)) {
        if (en.getValue().isEmpty()) {
          K k = en.getKey();
          remove(k);
        }
        break;
      }
  }

  public Set<Entry<K, V>> entrySet() {
    LinkedHashSet<Entry<K, V>> list = new LinkedHashSet<>();
    Iterator<Entry<K, V>> i = iterator();
    while (i.hasNext())
      list.add(i.next());
    return list;
  }

  public String toString() {
    return map.toString();
  }

  /**
   * Iterates through entries.
   */
  @Override
  public Iterator<Entry<K, V>> iterator() {
    return new Iterator<Map.Entry<K, V>>() {

      private Iterator<Map.Entry<K, ArrayList<V>>> iterator = map.entrySet().iterator();
      private Map.Entry<K, ArrayList<V>> currententry = iterator.next();
      private Iterator<V> narrowit = currententry.getValue().iterator();

      @Override
      public boolean hasNext() {
        return narrowit.hasNext() || iterator.hasNext();
      }

      @Override
      public Entry<K, V> next() {
        if (narrowit.hasNext())
          return Map.entry(currententry.getKey(), narrowit.next());
        currententry = iterator.next();
        narrowit = currententry.getValue().iterator();
        return Map.entry(currententry.getKey(), narrowit.next());
      }
    };
  }

}
