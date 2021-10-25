import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import MsnLib.Msn;

/**
 * Contains information and methods in relation to the English dictionary including a wide
 * collection of names.
 * 
 * @author Mason Marker
 * @version 1.1 - 06/11/2021
 */
public class Dictionary implements Iterable<String> {

  TreeMap<String, String> dict;
  String[] names;

  /**
   * Default constructor.
   */
  public Dictionary() {
    dict = new TreeMap<>();
    names = Msn.toLineArray(Msn.contentsOfNoEmptyLines("names.txt"));
    Stream.of(Msn.toLineArray(Msn.contentsOfNoEmptyLines("english3.txt"))).forEach(this::addWord);
  }

  /**
   * Adds a word to this Dictionary.
   * 
   * @param s the word to add
   */
  public void addWord(String s) {
    dict.put(s, null);
  }

  /**
   * Adds a definition to the specified word.
   * 
   * @param word the word
   * @param def the definition to attach to the word
   */
  public void addDef(String word, String def) {
    dict.put(word, def);
  }

  /**
   * Checks if this Dictionary contains the word or name specified.
   * 
   * @param s the String
   * @return whether the String is included or not
   */
  public boolean contains(String s) {
    if (!Msn.contains(getWords(), s) && !Msn.contains(names, s))
      return false;
    return true;
  }

  /**
   * Gets all words in this Dictionary.
   * 
   * @return the words
   */
  public String[] getWords() {
    return dict.keySet().toArray(String[]::new);
  }

  /**
   * Gets the definition of the specified word.
   * 
   * @param word the word
   * @return the definition attached to the word
   */
  public String getDefinition(String word) {
    return dict.get(word);
  }

  /**
   * Gets the definition for the specified entry.
   * 
   * @param entry the entry
   * @return the definition
   */
  public String getDefinition(Map.Entry<String, String> entry) {
    return entry.getValue();
  }

  /**
   * Gets the word for the specified entry.
   * 
   * @param entry the entry
   * @return the word
   */
  public String getWord(Map.Entry<String, String> entry) {
    return entry.getKey();
  }

  /**
   * Decides whether the String passed is in the English Dictionary or not.
   * 
   * @param s the String
   * @return whether the word is English
   */
  public boolean isWord(String s) {
    return dict.containsKey(s);
  }

  /**
   * Gets a HashMap of all words in this Dictionary that have a valid definition.
   * 
   * @return a HashMap<Word, Definition>
   */
  public TreeMap<String, String> entriesWithDefinitions() {
    TreeMap<String, String> map = new TreeMap<>();
    for (Map.Entry<String, String> entry : dict.entrySet())
      if (entry.getValue() != null)
        map.put(entry.getKey(), entry.getValue());
    return map;
  }

  /**
   * Gets the words in this Dictionary that start with the prefix specified.
   * 
   * @param prefix the prefix
   * @return a Map of the entries
   */
  public TreeMap<String, String> wordsThatStartWith(String prefix) {
    TreeMap<String, String> map = new TreeMap<>();
    for (Map.Entry<String, String> entry : dict.entrySet())
      if (entry.getKey().toLowerCase().startsWith(prefix.toLowerCase()))
        map.put(entry.getKey(), entry.getValue());
    return map;
  }

  /**
   * Gets the words in this Dictionary that end with the suffix specified.
   * 
   * @param suffix the suffix
   * @return a Map of the entries
   */
  public TreeMap<String, String> wordsThatEndWith(String suffix) {
    TreeMap<String, String> map = new TreeMap<>();
    for (Map.Entry<String, String> entry : dict.entrySet())
      if (entry.getKey().toLowerCase().endsWith(suffix.toLowerCase()))
        map.put(entry.getKey(), entry.getValue());
    return map;
  }

  /**
   * Gets the amount of vowels in a word.
   * 
   * @param word the word to analyze
   * @return the amount of vowels
   */
  public int countVowels(String word) {
    int count = 0;
    for (int i = 0; i < word.length(); i++)
      if (Msn.isVowel(word.charAt(i)))
        count++;
    return count;
  }

  /**
   * Gets the amount of consonants in a word.
   * 
   * @param word the word to analyze
   * @return the amount of consonants
   */
  public int countConsonants(String word) {
    int count = 0;
    for (int i = 0; i < word.length(); i++)
      if (Msn.isConsonant(word.charAt(i)))
        count++;
    return count;
  }

  /**
   * Prints the values in this Dictionary.
   */
  public void print() {
    dict.entrySet().forEach(System.out::println);
  }

  /**
   * Gets the names in this Dictionary.
   * 
   * @return the names
   */
  public String[] getNames() {
    return names;
  }

  /**
   * Gets the names in this Dictionary that start with the given prefix.
   * 
   * @param prefix the prefix
   * @return names that start with the prefix
   */
  public String[] namesThatStartWith(String prefix) {
    ArrayList<String> n = new ArrayList<>();
    for (int i = 0; i < names.length; i++)
      if (names[i].toLowerCase().startsWith(prefix.toLowerCase()))
        n.add(names[i]);
    return n.toArray(String[]::new);
  }

  /**
   * Gets the names in this Dictionary that end with the given suffix.
   * 
   * @param suffix the suffix
   * @return names that end with the suffix
   */
  public String[] namesThatEndWith(String suffix) {
    ArrayList<String> n = new ArrayList<>();
    for (int i = 0; i < names.length; i++)
      if (names[i].toLowerCase().endsWith(suffix.toLowerCase()))
        n.add(names[i]);
    return n.toArray(String[]::new);
  }

  /**
   * Checks whether the word passed exists in this Dictionary's collection of names.
   * 
   * @param word the word
   * @return whether the word is considered a name
   */
  public boolean isName(String word) {
    return Msn.containsIgnoreCase(names, word);
  }

  /**
   * String representation of this Dictionary.
   */
  public String toString() {
    return dict.toString();
  }

  /**
   * Iterator over the words in this Dictionary.
   */
  @Override
  public Iterator<String> iterator() {
    return dict.keySet().iterator();
  }

  /**
   * Removes all non-name entries in this Dictionary.
   */
  public void clear() {
    dict = new TreeMap<>();
  }

}
