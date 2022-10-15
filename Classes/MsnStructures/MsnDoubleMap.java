package MsnStructures;

import java.util.Set;


/**
 * K V K
 * K V K
 * K V K
 * K V
 * K V
 * 
 */

public class MsnDoubleMap<K, V> {

    MsnMap<K, V> map1;
    MsnMap<V, K> map2;

    public MsnDoubleMap() {
        map1 = new MsnMap<K, V>();
        map2 = new MsnMap<V, K>();
    }

    public void toTreeMap() {
        map1.toTreeMap();
        map2.toTreeMap();
    }

    public void toHashMap() {
        map1.toHashMap();
        map2.toHashMap();
    }

    public void put(K key, V value) {
        map1.put(key, value);
        map2.put(value, key);
    }

    public V valueFor(K key) {
        return map1.get(key);
    }

    public K keyFor(V value) {
        return map2.get(value);
    }

    public void removeKey(K key) {
        V value = map1.get(key);
        map1.remove(key);
        map2.remove(value);
    }

    public void removeValue(V value) {
        K key = map2.get(value);
        map1.remove(key);
        map2.remove(value);
    }

    public boolean containsKey(K key) {
        return map1.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map2.containsKey(value);
    }

    public int size() {
        return map1.size();
    }

    public boolean isEmpty() {
        return map1.isEmpty();
    }

    public void clear() {
        map1.clear();
        map2.clear();
    }

    public Set<K> keySet() {
        return map1.keySet();
    }

    public Set<V> valueSet() {
        return map2.keySet();
    }
}