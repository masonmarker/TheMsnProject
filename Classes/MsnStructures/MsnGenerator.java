package MsnStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import MsnLib.Msn;

/**
 * Provides basic generation functions for testing.
 * 
 * @author Mason Marker
 * @version 1.0 - 11/29/2021
 */
public class MsnGenerator {

  /**
   * Generates a Collection.
   * 
   * @param elements the amount of elements in the Collection
   * @return a new Collection
   */
  public static Collection<Integer> generateIntegerCollection(int elements) {
    ArrayList<Integer> l = new ArrayList<>();
    for (int i = 0; i < elements; i++)
      l.add(Msn.randomInt(0, 100));
    return l;
  }

  /**
   * Generates a Collection.
   * 
   * @param elements the amount of elements in the Collection
   * @return a new Collection
   */
  public static Collection<Double> generateDoubleCollection(int elements) {
    ArrayList<Double> l = new ArrayList<>();
    for (int i = 0; i < elements; i++)
      l.add(Msn.random(0, 100));
    return l;
  }

  /**
   * Generates a Collection.
   * 
   * @param elements the amount of elements in the Collection
   * @return a new Collection
   */
  public static Collection<Character> generateCharacterCollection(int elements) {
    ArrayList<Character> l = new ArrayList<>();
    for (int i = 0; i < elements; i++)
      l.add(Msn.randomLetter());
    return l;
  }

  /**
   * Generates a Collection.
   * 
   * @param elements the amount of elements in the Collection
   * @return a new Collection
   */
  public static Collection<Boolean> generateBooleanCollection(int elements) {
    ArrayList<Boolean> l = new ArrayList<>();
    for (int i = 0; i < elements; i++)
      l.add(Msn.coinflip());
    return l;
  }

  /**
   * Generates a Collection.
   * 
   * @param elements the amount of elements in the Collection
   * @return a new Collection
   */
  public static Collection<String> generateStringCollection(int elements) {
    ArrayList<String> l = new ArrayList<>();
    for (int i = 0; i < elements; i++)
      l.add(Msn.randomString(5));
    return l;
  }

  /**
   * Generates a Stream.
   * 
   * @param elements the amount of elements in the Stream
   * @return a new Stream
   */
  public static Stream<Integer> generateIntegerStream(int elements) {
    return generateIntegerCollection(elements).stream();
  }

  /**
   * Generates a Stream.
   * 
   * @param elements the amount of elements in the Stream
   * @return a new Stream
   */
  public static Stream<Double> generateDoubleStream(int elements) {
    return generateDoubleCollection(elements).stream();
  }

  /**
   * Generates a Stream.
   * 
   * @param elements the amount of elements in the Stream
   * @return a new Stream
   */
  public static Stream<Character> generateCharacterStream(int elements) {
    return generateCharacterCollection(elements).stream();
  }

  /**
   * Generates a Stream.
   * 
   * @param elements the amount of elements in the Stream
   * @return a new Stream
   */
  public static Stream<Boolean> generateBooleanStream(int elements) {
    return generateBooleanCollection(elements).stream();
  }

  /**
   * Generates a Stream.
   * 
   * @param elements the amount of elements in the Stream
   * @return a new Stream
   */
  public static Stream<String> generateStringStream(int elements) {
    return generateStringCollection(elements).stream();
  }

  /**
   * Attempts to populate a map with 'entries' amount of entries.
   * 
   * Map must have at least one entry in it before calling this method
   * 
   * @param <K> Key Generic
   * @param <V> Value Generic
   * 
   * @param entries the amount of random entries to generate
   */
  @SuppressWarnings("unchecked")
  public static <K, V> void populate(Map<K, V> map, int entries) {
    Map.Entry<K, V> first = new TreeMap<>(map).firstEntry();
    String keyType = Msn.identify(first.getKey());
    String valueType = Msn.identify(first.getValue());
    switch (keyType) {
      case "Integer":
        for (Integer i : generateIntegerCollection(entries))
          map.put((K) i, null);
        populateValues(map, valueType);
        break;
      case "Double":
        for (Double i : generateDoubleCollection(entries))
          map.put((K) i, null);
        populateValues(map, valueType);
        break;
      case "Character":
        for (Character i : generateCharacterCollection(entries))
          map.put((K) i, null);
        populateValues(map, valueType);
        break;
      case "Boolean":
        for (Boolean i : generateBooleanCollection(entries))
          map.put((K) i, null);
        populateValues(map, valueType);
        break;
      case "String":
        for (String i : generateStringCollection(entries))
          map.put((K) i, null);
        populateValues(map, valueType);
        break;
    }
  }

  @SuppressWarnings("unchecked")
  private static <K, V> void populateValues(Map<K, V> map, String valueType) {
    switch (valueType) {
      case "Integer":
        Collection<Integer> c = generateIntegerCollection(map.size());
        Iterator<Integer> it = c.iterator();
        for (Map.Entry<K, V> en : map.entrySet())
          map.put(en.getKey(), (V) it.next());
        break;
      case "Double":
        Collection<Double> c1 = generateDoubleCollection(map.size());
        Iterator<Double> it1 = c1.iterator();
        for (Map.Entry<K, V> en : map.entrySet())
          map.put(en.getKey(), (V) it1.next());
        break;
      case "Character":
        Collection<Character> c11 = generateCharacterCollection(map.size());
        Iterator<Character> it11 = c11.iterator();
        for (Map.Entry<K, V> en : map.entrySet())
          map.put(en.getKey(), (V) it11.next());
        break;
      case "Boolean":
        Collection<Boolean> c111 = generateBooleanCollection(map.size());
        Iterator<Boolean> it111 = c111.iterator();
        for (Map.Entry<K, V> en : map.entrySet())
          map.put(en.getKey(), (V) it111.next());
        break;
      case "String":
        Collection<String> c1111 = generateStringCollection(map.size());
        Iterator<String> it1111 = c1111.iterator();
        for (Map.Entry<K, V> en : map.entrySet())
          map.put(en.getKey(), (V) it1111.next());
        break;
    }
  }
}
