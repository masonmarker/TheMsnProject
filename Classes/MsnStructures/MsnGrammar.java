package MsnStructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import MsnLib.Msn;

/**
 * Grants the ability to create grammar.
 * 
 * @author Mason Marker
 * @version 1.0 - 12/06/2021
 */
public class MsnGrammar {

  MsnMultimap<Character, String> rules;

  public MsnGrammar() {
    rules = new MsnMultimap<>();
    rules.toLinkedHashMap();
  }

  public MsnGrammar(String string) {
    rules = new MsnMultimap<>();
    rules.toLinkedHashMap();
  }

  /**
   * Adds a rule to this grammar.
   * 
   * @param rule the rule to add
   */
  public void addRule(String nonterminal, String replacement) {
    rules.put(nonterminal.toUpperCase().charAt(0), replacement);
  }

  /**
   * Applies a single rule to the String specified.
   * 
   * @param nonterminal the nonterminal
   * @param replacement the replacement
   * @param string the String to fix
   * @return the fixed String
   */
  public static String applyRule(char nonterminal, String replacement, String string) {
    return string.replace("" + String.valueOf(nonterminal).toUpperCase().charAt(0), replacement);
  }

  /**
   * Applies a rule for a certain nonterminal at the replacement's index.
   * 
   * @param nonterminal the nonterminal
   * @param index the index of the rule
   * @return the fixed String
   */
  public String applyRule(char nonterminal, int index, String string) {
    String st = string;
    st = applyRule(nonterminal, rules.get(nonterminal).get(index), st);
    return st;
  }

  /**
   * Applies every rule for a specific nonterminal, sequentially.
   * 
   * @param nonterminal the nonterminal
   * @param string the String to fix
   * @return the fixed String
   */
  public String applyRuleSet(char nonterminal, String string) {
    String st = string;
    for (String s : rules.get(nonterminal))
      st = applyRule(nonterminal, s, st);
    return st;
  }

  /**
   * Applies every logged rule, sequentially.
   * 
   * @param s
   * @return
   */
  public String applyRules(String s) {
    String st = s;
    for (Map.Entry<Character, String> en : rules)
      st = applyRule(en.getKey(), en.getValue(), st);
    return st;
  }

  public boolean canTerminate() {
    for (Map.Entry<Character, ArrayList<String>> en : rules.getMap().entrySet())
      if (!en.getValue().contains(""))
        return false;
    return true;
  }

  public MsnMultimap<Character, String> getRules() {
    return rules;
  }

  public String toString() {
    return rules.toString();
  }


}
