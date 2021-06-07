import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Dictionary class, contains information and methods in relation to the English dictionary.
 * 
 * @author Mason Marker
 * @version 1.0 - 06/04/2021
 */
public class Dictionary {

  TreeMap<String, String> dict;

  /**
   * Default constructor.
   */
  public Dictionary() {
    dict = new TreeMap<>();
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
      if (entry.getKey().startsWith(prefix))
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
      if (entry.getKey().endsWith(suffix))
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
   * String representation of this Dictionary.
   */
  public String toString() {
    return dict.toString();
  }

}
