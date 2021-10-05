import MsnLib.Msn;

/**
 * Offers extensive random capabilities outside of the basic Dictionary functions.
 * 
 * @author Mason Marker
 * @version 1.0 - 06/11/2021
 */
public class DictionaryUtilities {

  private Dictionary dict;

  /**
   * Constructor.
   */
  public DictionaryUtilities() {
    dict = new Dictionary();
  }

  /**
   * Gets a random word from a Dictionary object.
   * 
   * @return a random word
   */
  public String nextWord() {
    return (String) Msn.randomElement(dict.getWords());
  }

  /**
   * Gets a random word with the specified length.
   * 
   * @param length the length of the word
   * @return a new word
   */
  public String nextWord(int length) {
    String word = nextWord();
    while (word.length() != length)
      word = nextWord();
    return word;
  }

  /**
   * Gets a random word from a Dictionary object with the given fix.
   * 
   * @param fix either a prefix or a suffix
   * @param prefix : true if the fix is a prefix, false if the fix is a suffix
   * @return a random word
   */
  public String nextWord(String fix, boolean prefix) {
    if (prefix)
      return (String) Msn
          .randomElement(dict.wordsThatStartWith(fix).keySet().toArray(String[]::new));
    return (String) Msn.randomElement(dict.wordsThatEndWith(fix).keySet().toArray(String[]::new));
  }

  /**
   * Gets a random name from a Dictionary object.
   * 
   * @return a random name
   */
  public String nextName() {
    return (String) Msn.randomElement(dict.getNames());
  }

  /**
   * Gets a random name with the specified length.
   * 
   * @param length the length of the name
   * @return a new name
   */
  public String nextName(int length) {
    String word = nextName();
    while (word.length() != length)
      word = nextName();
    return word;
  }

  /**
   * Gets a random name from a Dictionary object with the given fix.
   * 
   * @param fix either a prefix or a suffix
   * @param prefix : true if the fix is a prefix, false if the fix is a suffix
   * @return a random name
   */
  public String nextName(String fix, boolean prefix) {
    if (prefix)
      return (String) Msn.randomElement(dict.namesThatStartWith(fix));
    return (String) Msn.randomElement(dict.namesThatEndWith(fix));
  }

  /**
   * Gets a random full name from a Dictionary object.
   * 
   * @param addMiddleName whether to add a middle name or not
   * @return a random full name
   */
  public String nextFullName(boolean addMiddleName) {
    if (!addMiddleName)
      return nextName() + " " + nextName();
    return nextName() + " " + nextName() + " " + nextName();
  }

  /**
   * Gets a random full name from a Dictionary object.
   * 
   * @param addMiddleName whether to add a middle name or not
   * @return a random full name
   */
  public String nextFullName(String initials) {
    String name = "";
    for (int i = 0; i < initials.length(); i++)
      if (i != initials.length() - 1)
        name += nextName(String.valueOf(initials.charAt(i)), true) + " ";
      else
        name += nextName(String.valueOf(initials.charAt(i)), true);
    return name;
  }
}
